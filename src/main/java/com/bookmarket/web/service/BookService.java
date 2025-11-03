package com.bookmarket.web.service;

import com.bookmarket.web.dto.BookFormDto;
import com.bookmarket.web.entity.Book;
import com.bookmarket.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Value("${bookmarket.upload.path}") // application.properties에 설정 필요
    private String uploadPath;

    // 도서 등록 및 수정
    public Long saveBook(BookFormDto bookFormDto, MultipartFile bookImage) throws IOException {
        String imageUrl = null;
        if (bookImage != null && !bookImage.isEmpty()) {
            imageUrl = saveImage(bookImage);
        } else if (bookFormDto.getImageUrl() != null && !bookFormDto.getImageUrl().isEmpty()) {
            imageUrl = bookFormDto.getImageUrl(); // 기존 이미지 유지
        }

        Book book;
        if (bookFormDto.getId() == null) { // 신규 등록
            book = new Book();
        } else { // 수정
            book = bookRepository.findById(bookFormDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 도서가 없습니다. id=" + bookFormDto.getId()));
        }

        book.setTitle(bookFormDto.getTitle());
        book.setAuthor(bookFormDto.getAuthor());
        book.setPublisher(bookFormDto.getPublisher());
        book.setPrice(bookFormDto.getPrice());
        book.setStock(bookFormDto.getStock());
        book.setDescription(bookFormDto.getDescription());
        book.setImageUrl(imageUrl);

        bookRepository.save(book);
        return book.getId();
    }

    // 이미지 저장 로직
    private String saveImage(MultipartFile imageFile) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedFileName = uuid + extension;
        Path uploadDir = Paths.get(uploadPath);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(savedFileName);
        imageFile.transferTo(filePath.toFile());
        return "/images/" + savedFileName; // 웹에서 접근할 수 있는 URL 반환
    }

    // 모든 도서 조회
    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    // ID로 도서 조회
    @Transactional(readOnly = true)
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 도서가 없습니다. id=" + id));
    }

    // 도서 삭제
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}

package com.bookmarket.web.controller;

import com.bookmarket.web.dto.BookFormDto;
import com.bookmarket.web.entity.Book;
import com.bookmarket.web.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 도서 목록 페이지
    @GetMapping("/books")
    public String bookList(Model model) {
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "home"; // home.html 또는 books/list.html
    }

    // 도서 상세 페이지
    @GetMapping("/books/{id}")
    public String bookDetail(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "books/detail";
    }

    // 관리자용 도서 등록 폼
    @GetMapping("/admin/books/new")
    public String bookForm(Model model) {
        model.addAttribute("bookFormDto", new BookFormDto());
        return "admin/book_form";
    }

    // 관리자용 도서 수정 폼
    @GetMapping("/admin/books/{id}/edit")
    public String bookEditForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findBookById(id);
        BookFormDto bookFormDto = new BookFormDto();
        bookFormDto.setId(book.getId());
        bookFormDto.setTitle(book.getTitle());
        bookFormDto.setAuthor(book.getAuthor());
        bookFormDto.setPublisher(book.getPublisher());
        bookFormDto.setPrice(book.getPrice());
        bookFormDto.setStock(book.getStock());
        bookFormDto.setDescription(book.getDescription());
        bookFormDto.setImageUrl(book.getImageUrl()); // 기존 이미지 URL 전달

        model.addAttribute("bookFormDto", bookFormDto);
        return "admin/book_form";
    }

    // 도서 등록 및 수정 처리
    @PostMapping("/admin/books")
    public String saveBook(@Valid @ModelAttribute BookFormDto bookFormDto,
                           BindingResult bindingResult,
                           @RequestParam(value = "bookImage", required = false) MultipartFile bookImage,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/book_form";
        }
        try {
            bookService.saveBook(bookFormDto, bookImage);
        } catch (IOException e) {
            model.addAttribute("errorMessage", "이미지 처리 중 오류가 발생했습니다.");
            return "admin/book_form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/book_form";
        }
        return "redirect:/books";
    }

    // 도서 삭제
    @PostMapping("/admin/books/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}

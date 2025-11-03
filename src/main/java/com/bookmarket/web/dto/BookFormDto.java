package com.bookmarket.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BookFormDto {

    private Long id;

    @NotBlank(message = "도서명은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "저자는 필수 항목입니다.")
    private String author;

    @NotBlank(message = "출판사는 필수 항목입니다.")
    private String publisher;

    @NotNull(message = "가격은 필수 항목입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고는 필수 항목입니다.")
    @Min(value = 0, message = "재고는 0개 이상이어야 합니다.")
    private Integer stock;

    private String description;

    private MultipartFile bookImage;

    private String imageUrl; // 기존 이미지 URL
}
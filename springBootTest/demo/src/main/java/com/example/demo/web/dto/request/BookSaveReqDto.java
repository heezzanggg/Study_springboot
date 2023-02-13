package com.example.demo.web.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.domain.Book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookSaveReqDto {

    @Size(min = 1, max = 50)
    @NotBlank // Empty : 빈값은 안되나 대신에 Null이 들어감 , Blank : 빈값, null 다 안됨
    private String title;

    @Size(min = 1, max = 50)
    @NotBlank
    private String author;

    // reqDto -> Entity
    public Book toEntity() {
        return Book.builder().title(title).author(author).build();
    }
}

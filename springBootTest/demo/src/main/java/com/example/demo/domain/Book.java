package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.demo.web.dto.response.BookResDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity // 엔티티에는 setter 잘 안쓰지~ 데이터 자체 보호해야댕 ㅎㅎ
public class Book {

    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동증가
    @Id // pk
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String author;

    @Builder // 생성자에 빌더 달아요
    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    // Entity -> resDto 변환 메소드
    public BookResDto toDTO() {
        return BookResDto.builder().id(id).title(title).author(author).build();
    }

    public void update(String title, String author) {
        this.title = title;
        this.author = author;
    }

}

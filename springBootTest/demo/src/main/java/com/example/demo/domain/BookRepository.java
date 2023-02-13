package com.example.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    // <엔티티, pk타입>
    // 엔티티마다 리포지토리 만들어야되고 extends~ 반드시 있어야함

}

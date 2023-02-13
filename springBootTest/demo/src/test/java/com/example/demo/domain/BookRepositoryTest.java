package com.example.demo.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩, 다른건 안되용~
public class BookRepositoryTest {

    @Autowired // test에서 순환참조 에러 안잡힘.
    // Junit에서는 Autowired를 통해 DI를 진행해야함.
    private BookRepository bookRepository;

    // BeforeAll //: 테스트 시작전 단 한번만 시작
    @BeforeEach // 각각의 테스트 메서등 시작 전 한번만 시작하는 메서드
    public void 데이터_준비() {
        String title = "java";
        String author = "상희";
        Book book = Book.builder().title(title).author(author).build();
        Book bookPS = bookRepository.save(book);
    }

    @Test
    public void 책등록_test() {
        // given (데이터 준비)
        String title = "java";
        String author = "상희";
        Book book = Book.builder().title(title).author(author).build();
        // when(테스트 실행)
        Book bookPS = bookRepository.save(book);
        // thne(검증)
        assertEquals(title, bookPS.getTitle()); // 넣은값, 저장된값
        assertEquals(author, bookPS.getAuthor());
    }
    // 트랜젝션 종료(저장된 데이터를 초기화됨)

    @Test
    public void 책목록보기_test() {
        // given(데이터 준비)
        String title = "java";
        String author = "상희";
        // when(테스트 실행)
        List<Book> booksPS = bookRepository.findAll();// 전체목록 가져오기
        // then(테스트 검증)
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    public void 책_한권보기_test() {
        // given(준비)
        String title = "java";
        String author = "상희";
        // when(실행)
        Book bookOnePS = bookRepository.findById(1L).get();
        // optional null 가질수 있음,
        // then(검증)
        assertEquals(title, bookOnePS.getTitle());
        assertEquals(author, bookOnePS.getAuthor());
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    public void 책삭제_test() {
        // given(준비)
        Long id = 1L;
        // when(실행)
        bookRepository.deleteById(id);
        // then(검증)
        assertFalse(bookRepository.findById(id).isPresent());
        // ()내부문이 false가 되야 true반환!
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    public void 책수정_test() {
        // given(준비)
        Long id = 1L;
        String title = "스프링부트";
        String author = "성욱센세";
        Book book = Book.builder().id(id).title(title).author(author).build();
        // when(실행)
        Book bookPS = bookRepository.save(book);
        // then(검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    } // 더티체킹!

}

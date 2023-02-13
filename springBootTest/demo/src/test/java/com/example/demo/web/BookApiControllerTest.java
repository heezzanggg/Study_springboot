package com.example.demo.web;

import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.text.Document;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookRepository;
import com.example.demo.service.BookService;
import com.example.demo.web.dto.request.BookSaveReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

//통합테스트
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private BookRepository bookRepository;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {

        om = new ObjectMapper();
        headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach // 각각의 테스트 메서등 시작 전 한번만 시작하는 메서드
    public void 데이터_준비() {
        String title = "java";
        String author = "상희";
        Book book = Book.builder().title(title).author(author).build();
        bookRepository.save(book);
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    // 수정
    public void updateTest() throws Exception {
        // given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("제목1");
        bookSaveReqDto.setAuthor("지은이1");

        String body = om.writeValueAsString(bookSaveReqDto); // 프런트에서 넘어온 스트링형태의 랎

        // when
        HttpEntity<String> req = new HttpEntity<>(body, headers);

        ResponseEntity<String> res = rt.exchange("/api/v1/book/" + id, HttpMethod.PUT, req, String.class);

        // then(검증)
        DocumentContext dc = JsonPath.parse(res.getBody());
        System.out.println(res.getBody());
        String title = dc.read("$.body.title");

        assertThat(title).isEqualTo("제목1");
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    // 삭제
    public void deleteTest() {
        // given (준비)
        Integer id = 1;
        // when (실행)
        HttpEntity<String> req = new HttpEntity<>(null, headers);

        ResponseEntity<String> res = rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, req, String.class);

        // then (검증)
        DocumentContext dc = JsonPath.parse(res.getBody());

        Integer code = dc.read("$.code");

        assertThat(code).isEqualTo(1);
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    // 책한권만 가져오기
    public void getBookOneTest() {
        // given/(준비)
        Integer id = 1;
        // when(실행)
        HttpEntity<String> req = new HttpEntity<>(null, headers);

        ResponseEntity<String> res = rt.exchange("/api/v1/book/" + id, HttpMethod.GET, req, String.class);

        // then(검증)
        DocumentContext dc = JsonPath.parse(res.getBody()); // res.getBody()의 body는 내용물이 아니라 json형태의 header,body의 body임

        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("java");
    }

    @Sql("classpath:db/TableInit.sql")
    @Test
    public void getBookListTest() {
        // given(준비)
        // when(실행)
        HttpEntity<String> req = new HttpEntity<>(null, headers);

        ResponseEntity<String> res = rt.exchange("/api/v1/book", HttpMethod.GET, req, String.class);

        // then(검증)
        DocumentContext dc = JsonPath.parse(res.getBody()); // res.getBody()의 body는 내용물이 아니라 json형태의 header,body의 body임

        System.out.println(res.getBody());

        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("java");

    }

    @Test
    public void saveBookTest() throws Exception {
        // given(준비)
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("제목1");
        bookSaveReqDto.setAuthor("지은이1");

        String body = om.writeValueAsString(bookSaveReqDto);
        // when(실행)
        HttpEntity<String> req = new HttpEntity<>(body, headers);

        ResponseEntity<String> res = rt.exchange("/api/v1/book", HttpMethod.POST, req, String.class);

        // then(검증)
        DocumentContext dc = JsonPath.parse(res.getBody()); // res.getBody()의 body는 내용물이 아니라 json형태의 header,body의 body임

        System.out.println(res.getBody());

        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("제목1");
        assertThat(author).isEqualTo("지은이1");

    }

}

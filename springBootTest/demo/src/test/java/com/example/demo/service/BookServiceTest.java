package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookRepository;
import com.example.demo.web.dto.request.BookSaveReqDto;
import com.example.demo.web.dto.response.BookResDto;
import com.example.demo.web.dto.response.ListBookResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void 책등록_테스트() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto(); // BooksaveReqDto 에 빌더 안했으니까~
        dto.setTitle("책제목");
        dto.setAuthor("저자");
        // stub(가설)
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        // when
        BookResDto bookResDto = bookService.책등록(dto);
        // then
        assertThat(dto.getTitle()).isEqualTo(bookResDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookResDto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트() throws JsonProcessingException {
        // given(준비)
        // stub(가설)
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "책제목1", "저자1"));
        books.add(new Book(2L, "책제목2", "저자2"));
        books.add(new Book(3L, "책제목3", "저자3"));

        when(bookRepository.findAll()).thenReturn(books);
        // when
        ListBookResDto listBookResDto = bookService.책목록보기();
        // print
        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = mapper.writeValueAsString(listBookResDto); // listBookResDto json타입
        System.out.println(jsonStr);
        System.out.println(books);

        // then
        assertThat(listBookResDto.getItems().get(0).getTitle()).isEqualTo("책제목1");
    }

    @Test
    public void 책한권보기_테스트() {
        // given
        Long id = 1L;
        Book book = new Book(1L, "제목1", "지은이1");
        Optional<Book> bookOP = Optional.of(book); // 넣을때는 of로

        // stub
        when(bookRepository.findById(1L)).thenReturn(bookOP);
        // when
        BookResDto bookResDto = bookService.책한권보기(id);
        // then
        System.out.println(bookResDto.getTitle());

        assertThat(bookResDto.getTitle()).isEqualTo(book.getTitle());

    }

    @Test
    public void 책수정하기_테스트() {
        // given
        Long id = 1L;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("수정제목1");
        bookSaveReqDto.setAuthor("수정지은이1");

        // stub
        Book book = new Book(1L, "제목1", "지은이1");
        Optional<Book> bookOP = Optional.of(book);

        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookResDto bookResDto = bookService.책수정하기(id, bookSaveReqDto);

        // then
        assertThat(bookResDto.getTitle()).isEqualTo(bookSaveReqDto.getTitle());
        assertThat(bookResDto.getAuthor()).isEqualTo(bookSaveReqDto.getAuthor());
    }

}

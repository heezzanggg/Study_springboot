package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookRepository;
import com.example.demo.web.dto.request.BookSaveReqDto;
import com.example.demo.web.dto.response.BookResDto;
import com.example.demo.web.dto.response.ListBookResDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final키워드 붙은 객체 생성자 호출
@Service
public class BookService {

    private final BookRepository bookRepository;

    // 1.책등록
    @Transactional(rollbackFor = RuntimeException.class) // DB변경있을시 사용해야함.특정예외 지정
    public BookResDto 책등록(BookSaveReqDto bookSaveReqDto) {

        Book bookPS = bookRepository.save(bookSaveReqDto.toEntity());

        return bookPS.toDTO();
    }

    // 2.책 목록 보기
    // 스트림 - 맵 - 컬랙트
    public ListBookResDto 책목록보기() {

        List<BookResDto> dtos = bookRepository.findAll()
                .stream().map(Book::toDTO).collect(Collectors.toList());

        ListBookResDto listBookResDto = ListBookResDto.builder().items(dtos).build();
        // 타입 이름 = 타입.buyilder().~~~~.build();

        return listBookResDto;
    }

    // 3.책 한권 보기
    public BookResDto 책한권보기(Long id) {

        Optional<Book> bookOP = bookRepository.findById(id);// 옵셔널 엔티티 //옵셔널객체 bookOP

        if (bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            return bookPS.toDTO();
        } else {
            throw new RuntimeException("책한권보기 아이디 찾을수 없습니다");
        }
    }

    // 4.책 삭제하기
    @Transactional(rollbackFor = RuntimeException.class) // DB변경있을시 사용해야함.특정예외 지정
    public void 책삭제하기(Long id) {

        bookRepository.deleteById(id);

    }

    // 5.수정하기
    @Transactional(rollbackFor = RuntimeException.class) // DB변경있을시 사용해야함.특정예외 지정
    public BookResDto 책수정하기(Long id, BookSaveReqDto bookSaveReqDto) {

        Optional<Book> bookOP = bookRepository.findById(id);

        if (bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            bookPS.update(bookSaveReqDto.getTitle(), bookSaveReqDto.getAuthor());
            return bookPS.toDTO();
        } else {
            throw new RuntimeException("수정하기 아이디 찾을 수 없습니다");
        }
    }

}

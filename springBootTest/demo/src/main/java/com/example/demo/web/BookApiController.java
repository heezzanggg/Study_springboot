package com.example.demo.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.BookService;
import com.example.demo.web.dto.request.BookSaveReqDto;
import com.example.demo.web.dto.response.BookResDto;
import com.example.demo.web.dto.response.CMResDto;
import com.example.demo.web.dto.response.ListBookResDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    // 등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {
        // json타입으로 받을때 RequestBody 사용
        // Vaild 사용시에 나오는 오류 땜시 BindingResult 사용
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("+++++++++++++++++++++++++");
            System.out.println(errorMap.toString());
            System.out.println("+++++++++++++++++++++++++");

            throw new RuntimeException(errorMap.toString());
        }

        BookResDto bookResDto = bookService.책등록(bookSaveReqDto);

        return new ResponseEntity<>(CMResDto.builder().code(1).msg("책등록 성공").body(bookResDto).build(),
                HttpStatus.CREATED);
    }

    // 책 목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList() {
        ListBookResDto listBookResDto = bookService.책목록보기();

        return new ResponseEntity<>(CMResDto.builder().code(1).msg("책 목록 가져오기 성공").body(listBookResDto).build(),
                HttpStatus.OK);
    }

    // 책 한권 보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id) {
        // getmapping pathvariable 이용하니까 파라미터에 어노테이션 사용하기
        BookResDto bookResDto = bookService.책한권보기(id);

        return new ResponseEntity<>(CMResDto.builder().code(1).msg("책 한권 가져오기 성공").body(bookResDto).build(),
                HttpStatus.OK);

    }

    // 삭제하기
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBookOne(@PathVariable Long id) {

        bookService.책삭제하기(id);

        return new ResponseEntity<>(CMResDto.builder().code(1).msg("삭제성공").build(), HttpStatus.OK);
    }

    // 수정하기
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBooks(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("+++++++++++++++++++++++++");
            System.out.println(errorMap.toString());
            System.out.println("+++++++++++++++++++++++++");

            throw new RuntimeException(errorMap.toString());
        }

        BookResDto bookResDto = bookService.책수정하기(id, bookSaveReqDto);

        return new ResponseEntity<>(CMResDto.builder().code(1).msg("수정성공").body(bookResDto).build(), HttpStatus.OK);
    }
}

package com.example.demo.web.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ListBookResDto {

    List<BookResDto> items;

    @Builder
    public ListBookResDto(List<BookResDto> items) {
        this.items = items;
    }

}

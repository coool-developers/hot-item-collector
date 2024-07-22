package com.sparta.hotitemcollector.domain.product;

import lombok.Getter;

@Getter
public class ProductSimpleResponseDto {
    private Long id;
    private String name;
    private String image;
    private Long userId;
    private Long userName;
}

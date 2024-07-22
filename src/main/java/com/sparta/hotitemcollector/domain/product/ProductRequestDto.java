package com.sparta.hotitemcollector.domain.product;

import lombok.Getter;

@Getter
public class ProductRequestDto {

    private String name;
    private ProductCategory category;
    private String image;
    private Long price;
    private String info;

}

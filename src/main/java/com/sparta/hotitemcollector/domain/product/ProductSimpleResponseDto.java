package com.sparta.hotitemcollector.domain.product;

import lombok.Getter;

@Getter
public class ProductSimpleResponseDto {
    private Long id;
    private String name;
    private String image;
    private Long userId;

    public ProductSimpleResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.userId =product.getUser().getId();
    }
}

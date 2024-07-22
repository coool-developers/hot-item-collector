package com.sparta.hotitemcollector.domain.product;

import lombok.Getter;

@Getter
public class HotProductResponseDto {
    private Long id;
    private String name;

    public HotProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }
}

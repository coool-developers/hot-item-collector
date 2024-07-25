package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.Product;
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

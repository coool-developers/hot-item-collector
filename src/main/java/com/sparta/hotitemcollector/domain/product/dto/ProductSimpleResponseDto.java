package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import lombok.Getter;

@Getter
public class ProductSimpleResponseDto {

    private Long id;
    private String name;
    private ProductImageResponseDto image;
    private ProductStatus status;
    private Long userId;
    private String userName;

    public ProductSimpleResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImages().isEmpty() ? null
            : new ProductImageResponseDto(product.getImages().get(0));
        this.status=product.getStatus();
        this.userId = product.getUser().getId();
        this.userName = product.getUser().getNickname();
    }
}


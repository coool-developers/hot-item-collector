package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductSimpleResponseDto {

    private Long id;
    private String name;
    private List<ProductImageDto> images;
    private Long userId;
    private String userName;

    public ProductSimpleResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.images = product.getImages();
        this.userId = product.getUser().getId();
        this.userName = product.getUser().getNickname();
    }
}


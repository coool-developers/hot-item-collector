package com.sparta.hotitemcollector.domain.product;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private Long id;
    private String name;
    private ProductCategory category;
    private String image;
    private Long price;
    private String info;
    private Long likes;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.info = product.getInfo();
        this.likes = product.getLikes();
        this.userId = product.getUser().getId();
        this.createdAt = product.getCreatedAt();
        this.modifiedAt = product.getModifiedAt();
    }
}

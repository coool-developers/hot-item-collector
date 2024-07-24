package com.sparta.hotitemcollector.domain.product.dto;

import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private Long id;
    private String name;
    private ProductCategory category;
    private List<ProductImageDto> images;
    private Long price;
    private String info;
    private Long likes;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ProductResponseDto(Product product, List<ProductImageDto> images) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.images = images;
        this.price = product.getPrice();
        this.info = product.getInfo();
        this.likes = product.getLikes();
        this.userId = product.getUser().getId();
        this.createdAt = product.getCreatedAt();
        this.modifiedAt = product.getModifiedAt();
    }
}

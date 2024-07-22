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
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}

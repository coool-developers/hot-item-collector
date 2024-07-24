package com.sparta.hotitemcollector.domain.cart.dto;

import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItemResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private ProductImageResponseDto productImage;
    private Long price;
    private String productInfo;
    private ProductStatus productStatus;
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    public CartItemResponseDto(Long id, Long productId, String productName,
        ProductImageResponseDto productImage, Long price, String productInfo,
        ProductStatus productStatus, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
        this.productInfo = productInfo;
        this.productStatus = productStatus;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}

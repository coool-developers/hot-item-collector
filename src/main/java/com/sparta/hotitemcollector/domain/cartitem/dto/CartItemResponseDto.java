package com.sparta.hotitemcollector.domain.cartitem.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItemResponseDto {
	private Long id;
	private Long productId;
	private String productName;
	private String productImage;
	private Long price;
	private String productInfo;
	private Long cartId;
	private LocalDateTime createdAt;

	@Builder
	public CartItemResponseDto(Long id, Long productId, String productName, String productImage, Long price, String productInfo, Long cartId, LocalDateTime createdAt) {
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.price = price;
		this.productInfo = productInfo;
		this.cartId = cartId;
		this.createdAt = createdAt;
	}
}

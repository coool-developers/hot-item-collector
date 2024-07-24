package com.sparta.hotitemcollector.domain.cart.dto;

import java.time.LocalDateTime;

import com.sparta.hotitemcollector.domain.cart.CartItem;

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
	private String productStatus;
	private Long userId;
	private LocalDateTime createdAt;

	@Builder
	public CartItemResponseDto(Long id, Long productId, String productName, String productImage, Long price, String productInfo, String productStatus, Long userId, LocalDateTime createdAt) {
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

	public CartItemResponseDto(CartItem cartItem) {
		this.id = cartItem.getId();
		this.productId = cartItem.getProduct().getId();
		this.productName = cartItem.getProduct().getName();
		this.productImage = cartItem.getProduct().getImage();
		this.price = cartItem.getProduct().getPrice();
		this.productInfo = cartItem.getProduct().getInfo();
		this.productStatus = cartItem.getProduct().getStatus().getStatus();
		this.userId = cartItem.getUser().getId();
		this.createdAt = cartItem.getCreatedAt();
	}
}

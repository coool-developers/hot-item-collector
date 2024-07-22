package com.sparta.hotitemcollector.domain.cartitem.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItemResponseDto {
	private Long id;
	private Long productId;
	private Long cartId;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	@Builder
	public CartItemResponseDto (Long id, Long productId, Long cartId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.productId = productId;
		this.cartId = cartId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
}

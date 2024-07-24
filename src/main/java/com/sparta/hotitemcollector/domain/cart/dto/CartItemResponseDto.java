package com.sparta.hotitemcollector.domain.cart.dto;

import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import com.sparta.hotitemcollector.domain.cart.CartItem;
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

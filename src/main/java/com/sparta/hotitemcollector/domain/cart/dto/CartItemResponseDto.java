package com.sparta.hotitemcollector.domain.cart.dto;

import com.sparta.hotitemcollector.domain.cart.CartItem;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CartItemResponseDto {

	private Long id;
	private Long productId;
	private String productName;
	private ProductImageResponseDto productImage;
	private BigDecimal price;
	private String productInfo;
	private String productStatus;
	private Long userId;
	private String seller;
	private LocalDateTime createdAt;

	public CartItemResponseDto(CartItem cartItem) {
		this.id = cartItem.getId();
		this.productId = cartItem.getProduct().getId();
		this.productName = cartItem.getProduct().getName();
		this.productImage = cartItem.getProduct().getImages().isEmpty() ? null
			: new ProductImageResponseDto(cartItem.getProduct().getImages().get(0));
		this.price = cartItem.getProduct().getPrice();
		this.productInfo = cartItem.getProduct().getInfo();
		this.productStatus = cartItem.getProduct().getStatus().getStatus();
		this.userId = cartItem.getUser().getId();
		this.seller = cartItem.getUser().getNickname();
		this.createdAt = cartItem.getCreatedAt();
	}

}

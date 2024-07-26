package com.sparta.hotitemcollector.domain.order.dto;

import java.math.BigDecimal;

import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;

import lombok.Getter;

@Getter
public class OrderItemPrepareResponseDto {
	private Long productId;
	private String productName;
	private BigDecimal productPrice;
	private ProductImageResponseDto productImage;
	private Long sellerId;
	private String sellerName;

	public OrderItemPrepareResponseDto(Product product) {
		this.productId = product.getId();
		this.productName = product.getName();
		this.productPrice = product.getPrice();
		this.productImage = product.getImages().isEmpty() ? null
			: new ProductImageResponseDto(product.getImages().get(0));
		this.sellerId = product.getUser().getId();
		this.sellerName = product.getUser().getNickname();
	}

}

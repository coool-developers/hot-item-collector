package com.sparta.hotitemcollector.domain.order.dto;

import com.sparta.hotitemcollector.domain.orderitem.OrderItem;

import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItemResponseDto {

	private Long productId;
	private String productName;
	private ProductImageResponseDto productImage;
	private BigDecimal price;
	private String orderStatus;

	public OrderItemResponseDto(OrderItem orderItem) {
		this.productId = orderItem.getProduct().getId();
		this.productName = orderItem.getProduct().getName();
		this.productImage = orderItem.getProduct().getImages().isEmpty() ? null
			: new ProductImageResponseDto(orderItem.getProduct().getImages().get(0));
		this.price = orderItem.getProduct().getPrice();
		this.orderStatus = orderItem.getStatus().getStatus();
	}
}

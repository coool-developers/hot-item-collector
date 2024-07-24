package com.sparta.hotitemcollector.domain.order.dto;

import com.sparta.hotitemcollector.domain.orderitem.OrderItem;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemResponseDto {

	private Long productId;
	private String productName;
	private String productImage;
	private Long price;
	private String orderStatus;

	@Builder
	public OrderItemResponseDto(OrderItem orderItem) {
		this.productId = orderItem.getProduct().getId();
		this.productName = orderItem.getProduct().getName();
		this.productImage = orderItem.getProduct().getImage();
		this.price = orderItem.getProduct().getPrice();
		this.orderStatus = orderItem.getStatus().getStatus();
	}
}

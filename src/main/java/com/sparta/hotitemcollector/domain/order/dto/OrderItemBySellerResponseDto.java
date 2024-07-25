package com.sparta.hotitemcollector.domain.order.dto;

import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sparta.hotitemcollector.domain.orderitem.OrderItem;

import lombok.Getter;

@Getter
public class OrderItemBySellerResponseDto {

	private Long productId;
	private String productName;
	private ProductImageResponseDto productImage;
	private BigDecimal price;
	private String orderStatus;

	private Long orderId;
	private String userName;
	private String address;
	private String phoneNumber;
	private LocalDateTime createdAt;

	public OrderItemBySellerResponseDto(OrderItem orderItem) {
		this.productId = orderItem.getProduct().getId();
		this.productName = orderItem.getProduct().getName();
		this.productImage = orderItem.getProduct().getImages().isEmpty() ? null
			: new ProductImageResponseDto(orderItem.getProduct().getImages().get(0));
		this.price = orderItem.getProduct().getPrice();
		this.orderStatus = orderItem.getStatus().getStatus();

		this.orderId = orderItem.getOrder().getId();
		this.userName = orderItem.getOrder().getUserName();
		this.address = orderItem.getOrder().getAddress();
		this.phoneNumber = orderItem.getOrder().getPhoneNumber();
		this.createdAt = orderItem.getCreatedAt();
	}
}

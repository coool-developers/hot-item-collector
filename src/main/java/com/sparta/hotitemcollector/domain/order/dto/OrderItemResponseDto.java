package com.sparta.hotitemcollector.domain.order.dto;

import com.sparta.hotitemcollector.domain.orderitem.OrderItem;

import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class OrderItemResponseDto {

	private Long id;
	private Long productId;
	private String productName;
	private ProductImageResponseDto productImage;
	private BigDecimal price;
	private Long sellerId;
	private String sellerNickname;
	private String orderStatus;

	private Long orderId;
	private LocalDateTime createdAt;

	public OrderItemResponseDto(OrderItem orderItem) {
		this.id = orderItem.getId();
		this.productId = orderItem.getProduct().getId();
		this.productName = orderItem.getProduct().getName();
		this.productImage = orderItem.getProduct().getImages().isEmpty() ? null
			: new ProductImageResponseDto(orderItem.getProduct().getImages().get(0));
		this.price = orderItem.getProduct().getPrice();
		this.sellerId = orderItem.getProduct().getUser().getId();
		this.sellerNickname = orderItem.getProduct().getUser().getNickname();
		this.orderStatus = orderItem.getStatus().getStatus();
		this.createdAt = orderItem.getCreatedAt();

		//주문id
		this.orderId = orderItem.getOrder().getId();
	}
}

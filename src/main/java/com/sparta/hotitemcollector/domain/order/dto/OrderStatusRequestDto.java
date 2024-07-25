package com.sparta.hotitemcollector.domain.order.dto;

import com.sparta.hotitemcollector.domain.order.OrderStatus;

import lombok.Getter;

@Getter
public class OrderStatusRequestDto {
	OrderStatus status;
}

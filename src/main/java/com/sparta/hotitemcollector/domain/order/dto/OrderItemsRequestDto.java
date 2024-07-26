package com.sparta.hotitemcollector.domain.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class OrderItemsRequestDto {
	private List<Long> orderItemsId;
}

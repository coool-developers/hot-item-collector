package com.sparta.hotitemcollector.domain.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class OrderRequestDto {
	private String userName;
	private String address;
	private String phoneNumber;

	private List<Long> orderItemsId;
}

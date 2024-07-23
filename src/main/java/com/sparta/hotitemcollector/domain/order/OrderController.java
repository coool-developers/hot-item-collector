package com.sparta.hotitemcollector.domain.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.hotitemcollector.domain.order.dto.OrderRequestDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderResponseDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<CommonResponse> createOrder(@RequestBody OrderRequestDto orderRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OrderResponseDto responseDto = orderService.createOrder(orderRequestDto,
			userDetails.getUser());

		CommonResponse response = new CommonResponse("주문이 완료되었습니다.", 201, responseDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}


}

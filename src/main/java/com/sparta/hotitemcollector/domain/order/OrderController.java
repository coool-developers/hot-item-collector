package com.sparta.hotitemcollector.domain.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.hotitemcollector.domain.order.dto.OrderItemBySellerResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderRequestDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderStatusRequestDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/buy")
	public ResponseEntity<CommonResponse> createOrder(@RequestBody OrderRequestDto orderRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OrderResponseDto responseDto = orderService.createOrder(orderRequestDto,
			userDetails.getUser());

		CommonResponse response = new CommonResponse("주문이 완료되었습니다.", 201, responseDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/buy")
	public ResponseEntity<CommonResponse> getOrdersAllByBuyer(@RequestParam(defaultValue = "1") int page,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<OrderResponseDto> responseDtoList = orderService.getOrdersAllByBuyer(page, userDetails.getUser());

		CommonResponse responses = new CommonResponse("구매자의 주문 목록을 조회 성공했습니다.", 200, responseDtoList);
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}

	@GetMapping("/buy/{orderId}")
	public ResponseEntity<CommonResponse> getOrderByBuyer(@PathVariable("orderId") Long orderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OrderResponseDto responseDto = orderService.getOrderByBuyer(orderId, userDetails.getUser());

		CommonResponse response = new CommonResponse("구매자의 단건 주문을 조회 성공했습니다.", 200, responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/sell")
	public ResponseEntity<CommonResponse> getOrdersBySeller(@RequestParam(defaultValue = "1") int page,
		@RequestParam(required = false) OrderStatus status,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<OrderItemBySellerResponseDto> responseDtoList = orderService.getOrdersAllBySeller(page, status, userDetails.getUser());

		CommonResponse response = new CommonResponse("판매자의 주문 목록을 조회 성공했습니다.", 200, responseDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/sell/{orderItemId}")
	public ResponseEntity<CommonResponse> updateStatus(@PathVariable("orderItemId") Long orderItemId,
		@RequestBody OrderStatusRequestDto orderStatusRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		orderService.updateStatus(orderItemId, orderStatusRequestDto, userDetails.getUser());

		CommonResponse response = new CommonResponse("주문의 상태가 변경됐습니다.", 201, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

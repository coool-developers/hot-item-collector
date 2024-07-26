package com.sparta.hotitemcollector.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.hotitemcollector.domain.order.dto.OrderItemBySellerResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderItemPrepareResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderItemsRequestDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderStatusRequestDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@GetMapping("prepare/order")
	public ResponseEntity<CommonResponse<List<OrderItemPrepareResponseDto>>> getOrderPrepare(@RequestBody OrderItemsRequestDto orderItemsRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<OrderItemPrepareResponseDto> responseDto = orderService.getOrderPrepare(orderItemsRequestDto,
			userDetails.getUser());

		CommonResponse<List<OrderItemPrepareResponseDto>> response = new CommonResponse("주문입력페이지 조회를 성공했습니다.", 200, responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/orders/buy")
	public ResponseEntity<CommonResponse<List<OrderResponseDto>>> getOrdersAllByBuyer(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(3)}") LocalDateTime startDate,
		@RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now()}") LocalDateTime endDate,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<OrderResponseDto> responseDtoList = orderService.getOrdersAllByBuyer(page, size, startDate, endDate, userDetails.getUser());

		CommonResponse<List<OrderResponseDto>> responses = new CommonResponse("구매자의 주문 목록을 조회 성공했습니다.", 200, responseDtoList);
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}

	@GetMapping("/orders/buy/{orderId}")
	public ResponseEntity<CommonResponse<OrderResponseDto>> getOrderByBuyer(@PathVariable("orderId") Long orderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OrderResponseDto responseDto = orderService.getOrderByBuyer(orderId, userDetails.getUser());

		CommonResponse<OrderResponseDto> response = new CommonResponse("구매자의 단건 주문을 조회 성공했습니다.", 200, responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/orders/sell")
	public ResponseEntity<CommonResponse<List<OrderItemBySellerResponseDto>>> getOrdersBySeller(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(3)}") LocalDateTime startDate,
		@RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now()}") LocalDateTime endDate,
		@RequestParam(required = false) OrderStatus status,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<OrderItemBySellerResponseDto> responseDtoList = orderService.getOrdersAllBySeller(page, size, startDate, endDate, status, userDetails.getUser());

		CommonResponse<List<OrderItemBySellerResponseDto>> response = new CommonResponse("판매자의 주문 목록을 조회 성공했습니다.", 200, responseDtoList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/orders/sell/{orderItemId}")
	public ResponseEntity<CommonResponse> updateStatus(@PathVariable("orderItemId") Long orderItemId,
		@RequestBody OrderStatusRequestDto orderStatusRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		orderService.updateStatus(orderItemId, orderStatusRequestDto, userDetails.getUser());

		CommonResponse response = new CommonResponse("주문의 상태가 변경됐습니다.", 201, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}



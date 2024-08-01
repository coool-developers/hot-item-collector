package com.sparta.hotitemcollector.domain.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.sparta.hotitemcollector.domain.order.dto.OrderResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderStatusRequestDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@GetMapping("/orders/buy")
	public ResponseEntity<CommonResponse<Page<OrderResponseDto>>> getOrdersAllByBuyer(
		@RequestParam(defaultValue="1") int page,
		@RequestParam(defaultValue="4") int size,
		@RequestParam(defaultValue = "#{T(java.time.LocalDate).now().minusMonths(3)}")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Page<OrderResponseDto> responseDtoList = orderService.getOrdersAllByBuyer(page - 1, size, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), userDetails.getUser());

		CommonResponse<Page<OrderResponseDto>> responses = new CommonResponse("구매자의 주문 목록을 조회 성공했습니다.", 200, responseDtoList);
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}

	@GetMapping("/order/buy/{orderId}")
	public ResponseEntity<CommonResponse<OrderResponseDto>> getOrderByBuyer(@PathVariable("orderId") Long orderId,
																			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		OrderResponseDto responseDto = orderService.getOrderByBuyer(orderId, userDetails.getUser());

		CommonResponse<OrderResponseDto> response = new CommonResponse("구매자의 단건 주문을 조회 성공했습니다.", 200, responseDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/orders/sell")
	public ResponseEntity<CommonResponse<List<OrderItemBySellerResponseDto>>> getOrdersBySeller(
		@RequestParam(defaultValue = "#{T(java.time.LocalDate).now().minusMonths(3)}")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@RequestParam(required = false) String status,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<OrderItemBySellerResponseDto> responseDtoList = orderService.getOrdersAllBySeller(
			startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), status, userDetails.getUser());

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



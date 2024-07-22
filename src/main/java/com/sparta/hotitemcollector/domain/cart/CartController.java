package com.sparta.hotitemcollector.domain.cart;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.hotitemcollector.domain.cartitem.dto.CartItemResponseDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	@PostMapping("/carts/{productId}")
	public ResponseEntity<CommonResponse> createCartItem(@PathVariable("productId") Long productId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		CartItemResponseDto responseDto = cartService.createCartItem(userDetails.getUser(), productId);
		CommonResponse response = new CommonResponse<>("장바구니에 상품 담기 성공", 201, responseDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/carts/{productId}")
	public ResponseEntity<CommonResponse> deleteCartItem(@PathVariable("productId") Long productId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		cartService.deleteCartItem(userDetails.getUser(), productId);
		CommonResponse response = new CommonResponse<>("장바구니 상품 제거 성공", 204, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/carts")
	public ResponseEntity<CommonResponse> getCart(@RequestParam(defaultValue = "1") int page,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Page<CartItemResponseDto> responseDtoList = cartService.getCart(page, userDetails.getUser());

		CommonResponse response = new CommonResponse<>("장바구니 조회 성공", 200, responseDtoList);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}

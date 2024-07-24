package com.sparta.hotitemcollector.domain.cart;

import com.sparta.hotitemcollector.domain.cart.dto.CartItemResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.service.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import java.util.Collections;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartItemRepository cartItemRepository;
	private final ProductService productService;
	private final UserService userService;

	@Transactional
	public CartItemResponseDto createCartItem(User user, Long productId) {
		Product product = productService.findById(productId);

		if (user.getId().equals(product.getUser().getId())) {
			throw new CustomException(ErrorCode.SAME_USER_PRODUCT);
		}

		if (isCartItemExistAtUser(productId, user.getId())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_CARTITEM);
		}

		CartItem cartItem = CartItem.builder()
			.product(product)
			.user(user)
			.build();

		cartItemRepository.save(cartItem);

		return new CartItemResponseDto(cartItem);
	}

	@Transactional
	public void deleteCartItem(User user, Long productId) {
		Product product = productService.findById(productId);
		CartItem cartItem = findCartItemByProductIdAndUserId(productId, user.getId());

		cartItemRepository.delete(cartItem);
	}

	@Transactional(readOnly = true)
	public List<CartItemResponseDto> getCart(int page, User user) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		Page<CartItem> cartItemPage = cartItemRepository.findAllByUserId(user.getId(), pageable);

		return cartItemPage.getContent()
			.stream()
			.map(CartItemResponseDto::new)
			.collect(Collectors.toList());
	}

	public CartItem findCartItemByProductIdAndUserId(Long productId, Long userId) {
		return cartItemRepository.findCartItemByProductIdAndUserId(productId, userId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_CARTITEM)
		);
	}

	public boolean isCartItemExistAtUser(Long productId, Long userId) {
		return cartItemRepository.findCartItemByProductIdAndUserId(productId, userId).isPresent();
	}

}
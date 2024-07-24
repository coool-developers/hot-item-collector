package com.sparta.hotitemcollector.domain.cart;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.hotitemcollector.domain.cart.dto.CartItemResponseDto;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartItemRepository cartItemRepository;
	private final ProductService productService;
	private final UserService userService;

	@Transactional
	public CartItemResponseDto createCartItem(User user, Long productId) {
		Product product = productService.findById(productId);

		CartItem cartItem = CartItem.builder()
			.product(product)
			.user(user)
			.build();

		if (user.getId().equals(product.getUser().getId())) {
			throw new CustomException(ErrorCode.SAME_USER_PRODUCT);
		}

		if (isCartItemExistAtUser(productId, user.getId())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_CARTITEM);
		}

		cartItemRepository.save(cartItem);

		return CartItemResponseDto.builder()
			.id(cartItem.getId())
			.productId(cartItem.getProduct().getId())
			.productName(cartItem.getProduct().getName())
			.productImage(cartItem.getProduct().getImage())
			.price(cartItem.getProduct().getPrice())
			.productInfo(cartItem.getProduct().getInfo())
			.userId(cartItem.getUser().getId())
			.createdAt(cartItem.getCreatedAt())
			.build();
	}

	@Transactional
	public void deleteCartItem(User user, Long productId) {
		Product product = productService.findById(productId);
		CartItem cartItem = findCartItemByProductIdAndUserId(productId, user.getId());

		cartItemRepository.delete(cartItem);
	}

	@Transactional(readOnly = true)
	public Page<CartItemResponseDto> getCart(int page, User user) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		Page<CartItem> cartItemList = cartItemRepository.findAllByUserId(user.getId(), pageable);

		if (cartItemList.isEmpty()) {
			return new PageImpl<>(Collections.emptyList());
		}

		return cartItemList.map(cartItem -> CartItemResponseDto.builder()
			.id(cartItem.getId())
			.productId(cartItem.getProduct().getId())
			.productName(cartItem.getProduct().getName())
			.productImage(cartItem.getProduct().getImage())
			.price(cartItem.getProduct().getPrice())
			.productInfo(cartItem.getProduct().getInfo())
			.productStatus(cartItem.getProduct().getStatus())
			.userId(cartItem.getUser().getId())
			.createdAt(cartItem.getCreatedAt())
			.build());
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
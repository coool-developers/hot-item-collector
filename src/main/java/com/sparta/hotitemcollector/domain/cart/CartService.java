package com.sparta.hotitemcollector.domain.cart;

import org.springframework.stereotype.Service;

import com.sparta.hotitemcollector.domain.cartitem.CartItem;
import com.sparta.hotitemcollector.domain.cartitem.CartItemRepository;
import com.sparta.hotitemcollector.domain.cartitem.dto.CartItemResponseDto;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductRepository;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	// private final ProductService productService;
	private final ProductRepository productRepository;

	public CartItemResponseDto createCartItem(User user, Long productId) {
		// Product product = productService.findById(productId);
		Product product = productRepository.findById(productId).orElseThrow();
		Cart cart = findCartByUserId(user.getId());

		CartItem cartItem = CartItem.builder()
			.product(product)
			.cart(cart)
			.build();

		cartItemRepository.save(cartItem);

		return CartItemResponseDto.builder()
			.id(cartItem.getId())
			.productId(cartItem.getProduct().getId())
			.cartId(cartItem.getCart().getId())
			.createdAt(cartItem.getCreatedAt())
			.modifiedAt(cartItem.getModifiedAt())
			.build();
	}

	// 유저 만들 때 사용
	public void CreateCart(User user) {
		Cart cart = Cart.builder()
			.user(user)
			.build();

		cartRepository.save(cart);
	}

	public Cart findCartByUserId(Long userId) {
		return cartRepository.findCartByUserId(userId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_CART)
		);
	}

}
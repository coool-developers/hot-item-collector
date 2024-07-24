package com.sparta.hotitemcollector.domain.cart;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.hotitemcollector.domain.cart.dto.CartItemResponseDto;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductService productService;

	@Transactional
	public CartItemResponseDto createCartItem(User user, Long productId) {
		Product product = productService.findById(productId);
		Cart cart = findCartByUserId(user.getId());

		if (user.getId().equals(product.getUser().getId())) {
			throw new CustomException(ErrorCode.SAME_USER_PRODUCT);
		}

		if (isCartItemExistAtCart(productId, cart.getId())) {
			throw new CustomException(ErrorCode.ALREADY_EXIST_CARTITEM);
		}

		CartItem cartItem = CartItem.builder()
			.product(product)
			.cart(cart)
			.build();

		cartItemRepository.save(cartItem);

		return CartItemResponseDto.builder()
			.id(cartItem.getId())
			.productId(cartItem.getProduct().getId())
			.productName(cartItem.getProduct().getName())
			.productImage(cartItem.getProduct().getImage())
			.price(cartItem.getProduct().getPrice())
			.productInfo(cartItem.getProduct().getInfo())
			.cartId(cartItem.getCart().getId())
			.createdAt(cartItem.getCreatedAt())
			.build();
	}

	@Transactional
	public void deleteCartItem(User user, Long productId) {
		Product product = productService.findById(productId);
		Cart cart = findCartByUserId(user.getId());
		CartItem cartItem = findCartItemByProductIdAndCartId(productId, cart.getId());

		cartItemRepository.delete(cartItem);
	}

	@Transactional(readOnly = true)
	public List<CartItemResponseDto> getCart(int page, User user) {
		Cart cart = findCartByUserId(user.getId());
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		Page<CartItem> cartItemPage = cartItemRepository.findAllByCartId(cart.getId(), pageable);

		return cartItemPage.getContent()
			.stream()
			.map(CartItemResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public void createCart(User user) {
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

	public CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId) {
		return cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_CARTITEM)
		);
	}

	public boolean isCartItemExistAtCart(Long productId, Long cartId) {
		return cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId).isPresent();
	}

}
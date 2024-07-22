package com.sparta.hotitemcollector.domain.cartitem;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findCartItemByProductIdAndCartId(Long productId, Long cartId);
}

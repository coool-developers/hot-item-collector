package com.sparta.hotitemcollector.domain.cart;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findCartItemByProductIdAndCartId(Long productId, Long cartId);
	Page<CartItem> findAllByCartId(Long cartId, Pageable pageable);
}

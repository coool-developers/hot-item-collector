package com.sparta.hotitemcollector.domain.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findCartByUserId(Long userId);
}

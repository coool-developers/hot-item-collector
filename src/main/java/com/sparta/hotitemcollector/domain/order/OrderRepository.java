package com.sparta.hotitemcollector.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	Page<Orders> findAllByUserId(Long userId, Pageable pageable);
}

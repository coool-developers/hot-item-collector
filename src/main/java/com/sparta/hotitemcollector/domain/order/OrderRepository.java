package com.sparta.hotitemcollector.domain.order;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	Page<Orders> findAllByUserIdAndCreatedAtBetween(Long id, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

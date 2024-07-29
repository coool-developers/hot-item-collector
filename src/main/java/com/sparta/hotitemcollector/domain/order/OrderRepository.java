package com.sparta.hotitemcollector.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	List<Orders> findAllByUserIdAndCreatedAtBetween(Long id, LocalDateTime startDate, LocalDateTime endDate, Sort sort);
}

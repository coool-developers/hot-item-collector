package com.sparta.hotitemcollector.domain.orderitem;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sparta.hotitemcollector.domain.order.OrderStatus;
import com.sparta.hotitemcollector.domain.product.entity.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByOrderId(Long id);

	List<OrderItem> findAllByCreatedAtBetweenAndProductInOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, List<Product> productList);

	List<OrderItem> findAllByStatusAndCreatedAtBetweenAndProductInOrderByCreatedAtDesc(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, List<Product> productList);

	@Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.user.id = :userId "
		+ "AND o.createdAt BETWEEN :startDate AND :endDate")
	Page<OrderItem> findAllByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

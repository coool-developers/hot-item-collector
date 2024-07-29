package com.sparta.hotitemcollector.domain.orderitem;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.hotitemcollector.domain.order.OrderStatus;
import com.sparta.hotitemcollector.domain.product.entity.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByOrderId(Long id);

	List<OrderItem> findAllByCreatedAtBetweenAndProductIn(LocalDateTime startDate, LocalDateTime endDate, List<Product> productList, Sort sort);

	List<OrderItem> findAllByStatusAndCreatedAtBetweenAndProductIn(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, List<Product> productList, Sort sort);

}

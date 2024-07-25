package com.sparta.hotitemcollector.domain.orderitem;

import com.sparta.hotitemcollector.domain.product.entity.Product;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.hotitemcollector.domain.order.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	Page<OrderItem> findAllByProductIn(List<Product> productList, Pageable pageable);
	Page<OrderItem> findAllByStatusAndProductIn(OrderStatus status, List<Product> productList, Pageable pageable);

	List<OrderItem> findByOrderId(Long id);
}

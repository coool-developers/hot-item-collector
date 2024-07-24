package com.sparta.hotitemcollector.domain.orderitem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.hotitemcollector.domain.order.OrderStatus;
import com.sparta.hotitemcollector.domain.product.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	Page<OrderItem> findAllByProductIn(List<Product> productList, Pageable pageable);
	Page<OrderItem> findAllByStatusAndProductIn(OrderStatus status, List<Product> productList, Pageable pageable);
}

package com.sparta.hotitemcollector.domain.orderitem;

import com.sparta.hotitemcollector.domain.order.OrderStatus;
import com.sparta.hotitemcollector.domain.order.Orders;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.global.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor
public class OrderItem extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long id;

	@Column(name = "status", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Orders order;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Builder
	public OrderItem(long id, Orders order, Product product, OrderStatus status) {
		this.id = id;
		this.order = order;
		this.product = product;
		this.status = status;
	}

	public void updateOrderItemStatus(OrderStatus status) {
		this.status = status;
	}
}

package com.sparta.hotitemcollector.domain.orderitem;

import com.sparta.hotitemcollector.domain.order.Orders;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.global.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Orders order;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Builder
	public OrderItem(long id, Orders order, Product product) {
		this.id = id;
		this.order = order;
		this.product = product;
	}
}

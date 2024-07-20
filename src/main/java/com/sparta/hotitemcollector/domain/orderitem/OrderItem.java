package com.sparta.hotitemcollector.domain.orderitem;

import com.sparta.hotitemcollector.domain.order.Order;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
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
    @JoinColumn(name ="order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name ="product_id", nullable = false)
    private Product product;

    @Builder
    public OrderItem(long id, Order order, Product product) {
        this.id = id;
        this.order = order;
        this.product = product;
    }
}

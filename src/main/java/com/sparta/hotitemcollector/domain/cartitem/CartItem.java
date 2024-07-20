package com.sparta.hotitemcollector.domain.cartitem;

import com.sparta.hotitemcollector.domain.cart.Cart;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart_item")
@NoArgsConstructor
public class CartItem extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Builder
    public CartItem(Long id, Product product, Cart cart) {
        this.id = id;
        this.product = product;
        this.cart = cart;
    }
}

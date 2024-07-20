package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Like extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public Like(User user, Product product) {
        this.user = user;
        this.product = product;
    }

}

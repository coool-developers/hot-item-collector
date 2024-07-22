package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Entity
@Table(name = "product")
@RequiredArgsConstructor
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private Long likes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Product(ProductRequestDto requestDto, User user) {
        this.name = requestDto.getName();
        this.image = requestDto.getImage();
        this.price = requestDto.getPrice();
        this.info = requestDto.getInfo();
        this.category = requestDto.getCategory();
        this.likes = 0L;
        this.status = ProductStatus.ON_SALE;
        this.user = user;
    }

}

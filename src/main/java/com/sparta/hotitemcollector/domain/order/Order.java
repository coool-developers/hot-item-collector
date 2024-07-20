package com.sparta.hotitemcollector.domain.order;

import com.sparta.hotitemcollector.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column (name = "user_name", nullable = false)
    private String userName;

    @Column (name = "address", nullable = false)
    private String address;

    @Column (name = "phone_number",nullable = false)
    private String phoneNumber;

    @Column (name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Order(User user, String address, String phoneNumber, OrderStatus status, String userName) {
        this.user = user;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userName = userName;
    }
}

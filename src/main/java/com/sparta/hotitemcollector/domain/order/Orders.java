package com.sparta.hotitemcollector.domain.order;

import com.sparta.hotitemcollector.domain.payment.Payment;
import com.sparta.hotitemcollector.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Orders {
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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order")
    private List<Payment> paymentList;


    @Builder
    public Orders(User user, String address, String phoneNumber, OrderStatus status, String userName) {
        this.user = user;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userName = userName;
    }
}

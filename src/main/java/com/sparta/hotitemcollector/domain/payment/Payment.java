package com.sparta.hotitemcollector.domain.payment;

import com.sparta.hotitemcollector.domain.order.Orders;
import com.sparta.hotitemcollector.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    // 포트 원에서 결제 요청을 위해 요구하는 ID
    @Column(name = "merchant_uid", nullable = false, unique = true)
    private String merchantUid; // 주문 식별하기 위한 고유ID

    @Column(name = "imp_uid", nullable = false, unique = true)
    private String impUid; // 포트 원에서 생성하는 결제 고유 ID

    @Column(name = "pay_method", nullable = false)
    private String payMethod;

    @Column(name = "pay_amount", nullable = false)
    private BigDecimal payAmount;

    @Column(name = "status", nullable = false)
    private String status; // 결제 상태

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // 지불 일자

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Builder
    public Payment(String merchantUid, String impUid, String payMethod, BigDecimal payAmount, String status, LocalDateTime paidAt, Orders order) {
        this.merchantUid = merchantUid;
        this.impUid = impUid;
        this.payMethod = payMethod;
        this.payAmount = payAmount;
        this.status = status;
        this.paidAt = paidAt;
        this.order = order;
    }
}
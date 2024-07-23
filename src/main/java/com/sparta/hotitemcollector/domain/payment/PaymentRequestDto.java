package com.sparta.hotitemcollector.domain.payment;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRequestDto {
    private String pg;
    private String payMethod;
    private String merchantUid;
    private String name;
    private BigDecimal amount;
    private String buyerName;
    private String buyerTel;
    private String buyerAddr;

    @Builder
    public PaymentRequestDto(String pg, String payMethod, String merchantUid, String name, BigDecimal amount, String buyerName, String buyerTel, String buyerAddr) {
        this.pg = pg;
        this.payMethod = payMethod;
        this.merchantUid = merchantUid;
        this.name = name;
        this.amount = amount;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
    }
}

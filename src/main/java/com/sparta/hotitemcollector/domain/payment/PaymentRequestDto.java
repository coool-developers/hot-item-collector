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
    private String buyerEmail;
    private String buyerName;
    private String buyerTel;
    private String buyerAddr;
    private String buyerPostcode;

    @Builder
    public PaymentRequestDto(String pg, String payMethod, String merchantUid, String name, BigDecimal amount, String buyerEmail, String buyerName, String buyerTel, String buyerAddr, String buyerPostcode) {
        this.pg = pg;
        this.payMethod = payMethod;
        this.merchantUid = merchantUid;
        this.name = name;
        this.amount = amount;
        this.buyerEmail = buyerEmail;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.buyerPostcode = buyerPostcode;
    }
}

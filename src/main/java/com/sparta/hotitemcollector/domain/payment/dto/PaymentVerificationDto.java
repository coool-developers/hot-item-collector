package com.sparta.hotitemcollector.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentVerificationDto {
    private String impUid;
    private String merchantUid;
    private BigDecimal amount;  // 추가된 필드

    @Builder
    public PaymentVerificationDto(String impUid, String merchantUid, BigDecimal amount) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.amount = amount;  // 추가된 필드 초기화
    }
}
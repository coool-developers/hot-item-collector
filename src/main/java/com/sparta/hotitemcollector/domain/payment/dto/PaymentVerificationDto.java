package com.sparta.hotitemcollector.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentVerificationDto {
    private String impUid;
    private String merchantUid;

    @Builder
    public PaymentVerificationDto(String impUid, String merchantUid) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
    }
}
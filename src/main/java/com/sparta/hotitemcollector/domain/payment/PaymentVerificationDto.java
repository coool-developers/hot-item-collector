package com.sparta.hotitemcollector.domain.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentVerificationDto {
    private String imp_uid;
    private String merchant_uid;

    @Builder
    public PaymentVerificationDto(String imp_uid, String merchant_uid) {
        this.imp_uid = imp_uid;
        this.merchant_uid = merchant_uid;
    }
}
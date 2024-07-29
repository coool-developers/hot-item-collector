package com.sparta.hotitemcollector.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderPrepareRequestDto {
    private List<Long> cartItemList;
    private String buyerName;
    private String buyerTel;
    private String buyerAddr;

    @Builder
    public OrderPrepareRequestDto(List<Long> cartItemList, String buyerName, String buyerTel, String buyerAddr) {
        this.cartItemList = cartItemList;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
    }
}

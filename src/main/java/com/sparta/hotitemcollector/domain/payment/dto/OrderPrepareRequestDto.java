package com.sparta.hotitemcollector.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderPrepareRequestDto {
    private List<Long> productItemList;
    private String buyerName;
    private String buyerTel;
    private String buyerAddr;

    @Builder
    public OrderPrepareRequestDto(List<Long> productItemList, String buyerName, String buyerTel, String buyerAddr) {
        this.productItemList = productItemList;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
    }
}

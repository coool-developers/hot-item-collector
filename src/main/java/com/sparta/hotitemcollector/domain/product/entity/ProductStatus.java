package com.sparta.hotitemcollector.domain.product.entity;

public enum ProductStatus {
    ON_SALE("판매중"),
    SOLD_OUT("판매완료");

    private String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}

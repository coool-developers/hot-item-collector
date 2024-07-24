package com.sparta.hotitemcollector.domain.order;

public enum OrderStatus {
    SHIPMENT_START("배송 시작"),
    SHIPPING("배송 중"),
    SHIPPING_DONE("배송 완료"),
    ORDERED("주문 완료");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}

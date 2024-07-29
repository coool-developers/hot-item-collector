package com.sparta.hotitemcollector.domain.order;

import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;

public enum OrderStatus {
    SHIPMENT_START("배송 시작"),
    SHIPPING("배송 중"),
    SHIPPING_DONE("배송 완료"),
    ORDERED("주문 완료"),
    PAID("결제 완료"),
    PAID_READY("결제 대기중"),
    ORDER_CANCEL("주문 취소");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public static OrderStatus fromString(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.status.equals(status)) {
                return orderStatus;
            }
        }
        throw new CustomException(ErrorCode.NOT_FOUND_ORDERSTATUS);
    }

    public String getStatus() {
        return this.status;
    }
}

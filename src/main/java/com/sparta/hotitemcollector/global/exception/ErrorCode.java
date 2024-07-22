package com.sparta.hotitemcollector.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Basic HttpStatusCode
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),
    // 각 Service에서 필요한 ErrorCode 추가
    NON_EXISTENT_PRODUCT(HttpStatus.BAD_REQUEST, "아이디에 맞는 상품을 찾을 수 없습니다."),
    NOT_SAME_USER(HttpStatus.BAD_REQUEST, "사용자가 일치하지 않아 요청을 처리할 수 없습니다."),
    ALREADY_SOLD_OUT(HttpStatus.BAD_REQUEST, "이미 판매 완료된 상품입니다."),

    // Product
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "NOT FOUND PRODUCT"),

    // Cart
    NOT_FOUND_CART(HttpStatus.NOT_FOUND, "NOT FOUND CART"),
    NOT_FOUND_CARTITEM(HttpStatus.NOT_FOUND, "NOT FOUND CART_ITEM"),
    ALREADY_EXIST_CARTITEM(HttpStatus.BAD_REQUEST, "ALREADY_EXIST_CARTITEM"),
    SAME_USER_PRODUCT(HttpStatus.BAD_REQUEST, "SAME USER PRODUCT");


    private final HttpStatus status;
    private final String message;
}

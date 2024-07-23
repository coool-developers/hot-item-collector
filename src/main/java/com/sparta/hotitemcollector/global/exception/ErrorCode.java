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

    // Token
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 재로그인 해주세요."),
    NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    FALSE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "헤더가 잘못되었거나 누락되었습니다."),
    UNMATCHED_TOKEN(HttpStatus.BAD_REQUEST, "일치하지 않는 토큰입니다."),

    // Auth
    DUPLICATE_USER(HttpStatus.CONFLICT,"이미 등록된 사용자 입니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),


    // Product
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "NOT FOUND PRODUCT"),

    // Cart
    NOT_FOUND_CART(HttpStatus.NOT_FOUND, "NOT FOUND CART"),
    NOT_FOUND_CARTITEM(HttpStatus.NOT_FOUND, "NOT FOUND CART_ITEM"),
    ALREADY_EXIST_CARTITEM(HttpStatus.CONFLICT, "ALREADY_EXIST_CARTITEM"),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT FOUND USER"),

    // Follow
    ALREADY_EXIST_FOLLOW(HttpStatus.CONFLICT, "ALREADY_EXIST FOLLOW"),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "NOT FOUND FOLLOW");

    private final HttpStatus status;
    private final String message;
}

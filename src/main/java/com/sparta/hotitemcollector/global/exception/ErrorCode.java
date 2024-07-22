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

    // Product
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "NOT FOUND PRODUCT"),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT FOUND USER"),

    // Follow
    ALREADY_EXIST_FOLLOW(HttpStatus.CONFLICT, "ALREADY_EXIST FOLLOW"),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "NOT FOUND FOLLOW");

    private final HttpStatus status;
    private final String message;
}

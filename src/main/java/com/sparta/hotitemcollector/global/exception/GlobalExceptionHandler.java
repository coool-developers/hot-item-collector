package com.sparta.hotitemcollector.global.exception;

import com.sparta.hotitemcollector.global.common.CommonErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonErrorResponse> handleCustomException(final CustomException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                CommonErrorResponse.builder()
                        .message(e.getMessage())
                        .error(e.getStatusCode().getReasonPhrase())
                        .statusCode(e.getStatusCode().value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}

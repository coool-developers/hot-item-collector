package com.sparta.hotitemcollector.domain.payment;

import com.sparta.hotitemcollector.domain.payment.dto.OrderPrepareRequestDto;
import com.sparta.hotitemcollector.domain.payment.dto.PaymentRequestDto;
import com.sparta.hotitemcollector.domain.payment.dto.PaymentVerificationDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/api/payments/verify")
    public ResponseEntity<CommonResponse<String>> verifyPayment(@RequestBody PaymentVerificationDto verificationDto) {
//        paymentService.verifyPayment(verificationDto);
//        return ResponseEntity.ok(new CommonResponse<>("결제 검증 완료", 200, "결제 검증 성공"));
        return null;
    }

    // ToDo : 추후에 order로 리팩토링 진행
    @PostMapping("/prepare/order")
    public ResponseEntity<CommonResponse> prepareOrder(@RequestBody OrderPrepareRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long orderId = paymentService.prepareOrder(userDetails.getUser(), requestDto);

        CommonResponse response = new CommonResponse<>("임시 결제 테이블 생성 완료", 200, orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/prepare/payment")
    public ResponseEntity<CommonResponse> preparePayment(@RequestParam Long orderId) {
        PaymentRequestDto paymentRequest = paymentService.getPaymentRequestDataByOrderId(orderId);

        CommonResponse response = new CommonResponse<>("결제 데이터 준비 완료", 200, paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

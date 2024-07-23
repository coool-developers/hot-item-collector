package com.sparta.hotitemcollector.domain.payment;

import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/api/payments/prepare-payment")
    public ResponseEntity<CommonResponse<PaymentRequestDto>> getPreparePayment(@RequestParam String temporaryPaymentId) {
//        PaymentRequestDto paymentRequest = paymentService.getPaymentRequestData(temporaryPaymentId);
//        return ResponseEntity.ok(new CommonResponse<>("결제 데이터 준비 완료", 200, paymentRequest));
        return null;
    }

    @PostMapping("/api/payments/verify")
    public ResponseEntity<CommonResponse<String>> verifyPayment(@RequestBody PaymentVerificationDto verificationDto) {
//        paymentService.verifyPayment(verificationDto);
//        return ResponseEntity.ok(new CommonResponse<>("결제 검증 완료", 200, "결제 검증 성공"));
        return null;
    }
}

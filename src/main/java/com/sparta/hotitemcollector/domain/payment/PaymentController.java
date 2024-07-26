package com.sparta.hotitemcollector.domain.payment;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.sparta.hotitemcollector.domain.payment.dto.OrderPrepareRequestDto;
import com.sparta.hotitemcollector.domain.payment.dto.PaymentRequestDto;
import com.sparta.hotitemcollector.domain.payment.dto.PaymentVerificationDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import com.sparta.hotitemcollector.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments/verify")
    public ResponseEntity<CommonResponse<String>> verifyPayment(@RequestBody PaymentVerificationDto verificationDto) throws IamportResponseException, IOException {
        paymentService.verifyPayment(verificationDto);
        return ResponseEntity.ok(new CommonResponse<>("결제 검증 완료", 200, "결제 검증 성공"));
    }

    // ToDo : 추후에 order로 리팩토링 진행
    @PostMapping("/prepare/order")
    public ResponseEntity<CommonResponse> prepareOrder(@RequestBody OrderPrepareRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long orderId = paymentService.prepareOrder(userDetails.getUser(), requestDto);

        CommonResponse response = new CommonResponse<>("주문에 필요한 정보 입력 완료", 200, orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/prepare/payment")
    public ResponseEntity<CommonResponse> preparePayment(@RequestParam Long orderId) {
        PaymentRequestDto paymentRequest = paymentService.getPaymentRequestDataByOrderId(orderId);

        CommonResponse response = new CommonResponse<>("결제 데이터 준비 완료", 200, paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/payments/cancel")
    public ResponseEntity<CommonResponse<String>> cancelPayment(@RequestParam Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            boolean result = paymentService.cancelPayment(orderId, userDetails.getUser().getId());
            if (result) {
                CommonResponse<String> response = new CommonResponse<>("결제 취소 성공", 200, "");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                CommonResponse<String> response = new CommonResponse<>("결제 취소 중 오류가 발생했습니다.", 500, "");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IamportResponseException | IOException e) {
            CommonResponse<String> response = new CommonResponse<>("결제 취소 중 오류가 발생했습니다.", 500, "");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CustomException e) {
            CommonResponse<String> response = new CommonResponse<>(e.getMessage(), 400, "");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (SecurityException e) {
            CommonResponse<String> response = new CommonResponse<>(e.getMessage(), 403, "");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            CommonResponse<String> response = new CommonResponse<>("알 수 없는 오류가 발생했습니다." + e.getMessage(), 500, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

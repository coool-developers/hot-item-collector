package com.sparta.hotitemcollector.domain.payment;

import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private IamportClient iamportClient;

    @Value("${REST_API_KEY}")
    private String apiKey;

    @Value("${REST_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

//    public String createTemporaryPayment(OrderRequestDto orderRequestDto) {
//        // 임시 결제 데이터 저장
//        return temporaryPaymentId;
//    }
//
//    public PaymentRequestDto getPaymentRequestData(String temporaryPaymentId) {
//        // 임시 결제 데이터 조회
//        return paymentRequestDto;
//    }
//
//    public void verifyPayment(PaymentVerificationDto verificationDto) {
//        // 아임포트 API를 호출하여 결제 검증 수행
//        // 결제 상태 업데이트
//    }

}

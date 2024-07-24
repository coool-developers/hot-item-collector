package com.sparta.hotitemcollector.domain.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.sparta.hotitemcollector.domain.cart.CartItemRepository;
import com.sparta.hotitemcollector.domain.order.OrderRepository;
import com.sparta.hotitemcollector.domain.order.OrderStatus;
import com.sparta.hotitemcollector.domain.order.Orders;
import com.sparta.hotitemcollector.domain.orderitem.OrderItem;
import com.sparta.hotitemcollector.domain.orderitem.OrderItemRepository;
import com.sparta.hotitemcollector.domain.payment.dto.OrderPrepareRequestDto;
import com.sparta.hotitemcollector.domain.payment.dto.PaymentRequestDto;
import com.sparta.hotitemcollector.domain.payment.dto.PaymentVerificationDto;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductRepository;
import com.sparta.hotitemcollector.domain.product.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private IamportClient iamportClient;

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Value("${REST_API_KEY}")
    private String apiKey;

    @Value("${REST_SECRET_KEY}")
    private String secretKey;

    @Value("${IMP_UID}")
    private String uid;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @Transactional
    public Long prepareOrder(User user, OrderPrepareRequestDto requestDto) {
        Orders order = Orders.builder()
                .user(user)
                .address(requestDto.getBuyerAddr())
                .phoneNumber(requestDto.getBuyerTel())
                .userName(requestDto.getBuyerName())
                .status(OrderStatus.ORDERED)
                .build();
        orderRepository.save(order);

        // 임시 결제 테이블 생성
        requestDto.getCartItemList().forEach(itemId -> {
            Product product = cartItemRepository.findById(itemId).get().getProduct();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .build();

            orderItemRepository.save(orderItem);

            // 각 itemId로 제품 정보를 조회하여 Payment 생성
            Payment payment = Payment.builder()
                    .merchantUid("merchant_" + System.currentTimeMillis())
                    .impUid(uid)
                    .payMethod("card")
                    .amount(product.getPrice())
                    .status("READY")
                    .paidAt(null)
                    .order(order)
                    .orderItem(orderItem)
                    .build();
            paymentRepository.save(payment);
        });

        return order.getId();
    }

    public PaymentRequestDto getPaymentRequestDataByOrderId(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다: " + orderId));

        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        Payment payment = payments.get(0);

        return PaymentRequestDto.builder()
                .pg("html5_inicis")
                .payMethod(payment.getPayMethod())
                .merchantUid(payment.getMerchantUid())
                .amount(payment.getAmount())
                .name(payment.getOrderItem().getProduct().getName())
                .buyerName(order.getUserName())
                .buyerAddr(order.getAddress())
                .buyerTel(order.getPhoneNumber())
                .build();
    }


    public void verifyPayment(PaymentVerificationDto verificationDto) {
//        try {
//            IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.paymentByImpUid(verificationDto.getImpUid());
//            com.siot.IamportRestClient.response.Payment paymentResponse = response.getResponse();
//
//            Payment payment = paymentRepository.findByImpUid(verificationDto.getImpUid())
//                    .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다: " + verificationDto.getImpUid()));
//
//            if (paymentResponse.getStatus().equals("paid") && paymentResponse.getAmount().equals(payment.getAmount())) {
//                payment.setStatus("PAID");
//                payment.setImpUid(verificationDto.getImpUid());
//                payment.setPaidAt(LocalDateTime.now());
//                paymentRepository.save(payment);
//            } else {
//                throw new IllegalStateException("결제 검증 실패");
//            }
//        } catch (IamportResponseException | IOException e) {
//            throw new IllegalStateException("결제 검증 중 오류 발생", e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}

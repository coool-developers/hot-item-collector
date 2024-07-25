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
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.service.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .build();
        orderRepository.save(order);

        long totalAmount = 0; // 총 결제 금액을 저장할 변수

        for (Long itemId : requestDto.getCartItemList()) {
            Product product = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다: " + itemId))
                    .getProduct();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .status(OrderStatus.ORDERED)
                    .build();

            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);

            totalAmount += product.getPrice();
        }

        // 하나의 결제 레코드를 생성
        Payment payment = Payment.builder()
                .merchantUid("merchant_" + System.currentTimeMillis())
                .impUid("pending") // 결제 후 업데이트
                .payMethod("card")
                .amount(totalAmount) // 총 결제 금액
                .status("READY")
                .paidAt(null)
                .order(order)
                .build();
        paymentRepository.save(payment);

        return order.getId();
    }

    public PaymentRequestDto getPaymentRequestDataByOrderId(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다: " + orderId));

        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        Long totalAmount = payments.stream()
                .mapToLong(Payment::getAmount)
                .sum();

        String productName = payments.stream()
                .map(payment -> payment.getOrder().getOrderItems().get(0).getProduct().getName())
                .collect(Collectors.joining(", "));

        return PaymentRequestDto.builder()
                .pg("html5_inicis")
                .payMethod("card")
                .merchantUid(payments.get(0).getMerchantUid())
                .amount(totalAmount)
                .name(productName)
                .buyerName(order.getUserName())
                .buyerAddr(order.getAddress())
                .buyerTel(order.getPhoneNumber())
                .build();
    }

    @Transactional
    public void verifyPayment(PaymentVerificationDto verificationDto) throws IamportResponseException, IOException {
        // Iamport 결제 검증
        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = iamportClient.paymentByImpUid(verificationDto.getImpUid());
        com.siot.IamportRestClient.response.Payment iamportPayment = iamportResponse.getResponse();

        if (iamportPayment.getAmount().equals(verificationDto.getAmount())) {
            // 결제 완료 처리
            Payment payment = paymentRepository.findByMerchantUid(verificationDto.getMerchantUid())
                    .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다: " + verificationDto.getMerchantUid()));

            payment.updatePayment(verificationDto.getImpUid(), "PAID", LocalDateTime.now());
            paymentRepository.save(payment);

            // 해당 Order의 모든 Payment 상태를 변경
            List<Payment> payments = paymentRepository.findByOrderId(payment.getOrder().getId());
            payments.forEach(p -> {
                p.updatePayment(p.getImpUid(), "PAID", LocalDateTime.now());
            });
            paymentRepository.saveAll(payments);

            // 해당 Order의 모든 OrderItem 상태를 변경
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(payment.getOrder().getId());
            orderItems.forEach(orderItem -> orderItem.updateOrderItemStatus(OrderStatus.PAID));
            orderItemRepository.saveAll(orderItems);

        } else {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }
    }
}

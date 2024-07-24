package com.sparta.hotitemcollector.domain.order;

import static com.sparta.hotitemcollector.domain.order.OrderStatus.SHIPMENT_START;
import static com.sparta.hotitemcollector.domain.product.ProductStatus.SOLD_OUT;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.hotitemcollector.domain.cart.CartService;
import com.sparta.hotitemcollector.domain.order.dto.OrderItemResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderRequestDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderResponseDto;
import com.sparta.hotitemcollector.domain.order.dto.OrderStatusRequestDto;
import com.sparta.hotitemcollector.domain.orderitem.OrderItem;
import com.sparta.hotitemcollector.domain.orderitem.OrderItemRepository;
import com.sparta.hotitemcollector.domain.product.Product;
import com.sparta.hotitemcollector.domain.product.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductService productService;
	private final CartService cartService;

	@Transactional
	public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {

		// 주문 생성
		Orders order = Orders.builder()
			.userName(orderRequestDto.getUserName())
			.address(orderRequestDto.getAddress())
			.user(user)
			.phoneNumber(orderRequestDto.getPhoneNumber())
			.build();
		orderRepository.save(order);

		// 주문한 아이템들 생성
		orderRequestDto.getOrderItemsId().forEach(id -> {
			Product product = productService.findById(id);

			if (user.getId().equals(product.getUser().getId())) {
				throw new CustomException(ErrorCode.SAME_USER_PRODUCT);
			}
			if (product.getStatus() == SOLD_OUT) {
				throw new CustomException(ErrorCode.ALREADY_SOLD_OUT);
			}

			OrderItem orderItem = OrderItem.builder()
				.product(product)
				.order(order)
				.status(SHIPMENT_START)
				.build();
			orderItemRepository.save(orderItem);
			order.addOrderItem(orderItem);
			cartService.deleteCartItem(user, id);
			// TODO: productService. productStatus SOLD_OUT으로 수정해야함

		});

		OrderResponseDto orderItemResponseDto = OrderResponseDto.builder()
			.id(order.getId())
			.userName(order.getUserName())
			.address(order.getAddress())
			.phoneNumber(order.getPhoneNumber())
			.createdAt(order.getCreatedAt())
			.orderItemResponseDtoList(order.getOrderItems().stream().map(OrderItemResponseDto::new).toList())
			.build();

		return orderItemResponseDto;
	}

	@Transactional
	public void updateStatus(Long orderItemId, OrderStatusRequestDto orderStatusRequestDto, User user) {
		OrderItem orderItem = findOrderItemById(orderItemId);

		if (!user.getId().equals(orderItem.getProduct().getUser().getId())) {
			throw new CustomException(ErrorCode.NOT_SAME_USER);
		}
		OrderStatus status = orderStatusRequestDto.getStatus();
		orderItem.updateOrderItemStatus(status);

	}

	public Orders findOrdersById(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_ORDER)
		);
	}

	public OrderItem findOrderItemById(Long orderItemId) {
		return orderItemRepository.findById(orderItemId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_ORDERITEM)
		);
	}
}

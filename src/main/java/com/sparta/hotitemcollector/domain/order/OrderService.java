package com.sparta.hotitemcollector.domain.order;

import static com.sparta.hotitemcollector.domain.order.OrderStatus.SHIPMENT_START;
import static com.sparta.hotitemcollector.domain.product.ProductStatus.SOLD_OUT;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.hotitemcollector.domain.cart.CartService;
import com.sparta.hotitemcollector.domain.order.dto.OrderItemBySellerResponseDto;
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

		Orders order = Orders.builder()
			.userName(orderRequestDto.getUserName())
			.address(orderRequestDto.getAddress())
			.user(user)
			.phoneNumber(orderRequestDto.getPhoneNumber())
			.build();
		orderRepository.save(order);

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
			productService.updateStatus(product.getId());
		});

		return new OrderResponseDto(order);
	}

	@Transactional(readOnly = true)
	public List<OrderResponseDto> getOrdersAllByBuyer(int page, User user) {

		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(page - 1, 5, sort);

		Page<Orders> orderList = orderRepository.findAllByUserId(user.getId(), pageable);

		return orderList.getContent()
			.stream()
			.map(OrderResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public OrderResponseDto getOrderByBuyer(Long orderId, User user) {
		Orders order = findOrderById(orderId);

		if (!user.getId().equals(order.getUser().getId())) {
			throw new CustomException(ErrorCode.NOT_SAME_USER);
		}

		return new OrderResponseDto(order);
	}

	@Transactional(readOnly = true)
	public List<OrderItemBySellerResponseDto> getOrdersAllBySeller(int page, OrderStatus status, User user) {

		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(page - 1, 5, sort);
		Page<OrderItem> orderItemPage = Page.empty(pageable);
		List<Product> productList = productService.findByUserAndStatus(user, SOLD_OUT);

		if (status == null) {
			orderItemPage = orderItemRepository.findAllByProductIn(productList, pageable);
		}
		if (status != null) {
			orderItemPage = orderItemRepository.findAllByStatusAndProductIn(status, productList, pageable);
		}

		return orderItemPage.getContent()
			.stream()
			.map(OrderItemBySellerResponseDto::new)
			.collect(Collectors.toList());
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

	public Orders findOrderById(Long orderId) {
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

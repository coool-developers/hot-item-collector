package com.sparta.hotitemcollector.domain.cart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.sparta.hotitemcollector.TestConfig;
import com.sparta.hotitemcollector.domain.cart.dto.CartItemResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import com.sparta.hotitemcollector.domain.product.service.ProductService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;

@ExtendWith(MockitoExtension.class)
@Rollback(value = false)
@Import(TestConfig.class)
public class CartServiceTest {

	@InjectMocks
	CartService cartService;

	@Mock
	CartItemRepository cartItemRepository;
	@Mock
	ProductService productService;
	@Mock
	UserService userService;

	@DisplayName("1. 카트아이템 조회")
	@Test
	void getCartItemsTest() {

		//given
		int page = 1;
		int size = 10;

		User mockUser = Mockito.mock(User.class);
		Long userId = 1L;
		given(mockUser.getId()).willReturn(userId);

		Product mockProduct1 = Mockito.mock(Product.class);
		given(mockProduct1.getUser()).willReturn(mockUser);
		Product mockProduct2 = Mockito.mock(Product.class);
		given(mockProduct2.getUser()).willReturn(mockUser);

		ProductStatus mockStatus = Mockito.mock(ProductStatus.class);

		Long mockCartItemId1 = 1L;
		Long mockCartItemId2 = 2L;
		CartItem mockCartItem1 = Mockito.mock(CartItem.class);
		given(mockCartItem1.getId()).willReturn(mockCartItemId1);
		given(mockCartItem1.getProduct()).willReturn(mockProduct1);
		given(mockProduct1.getStatus()).willReturn(mockStatus);
		CartItem mockCartItem2 = Mockito.mock(CartItem.class);
		given(mockCartItem2.getId()).willReturn(mockCartItemId2);
		given(mockCartItem2.getProduct()).willReturn(mockProduct2);
		given(mockProduct2.getStatus()).willReturn(mockStatus);

		Page<CartItem> cartItemPage = new PageImpl<>(List.of(mockCartItem1, mockCartItem2));
		given(cartItemRepository.findAllByUserId(eq(userId), any(Pageable.class))).willReturn(cartItemPage);

		//when
		List<CartItemResponseDto> result = cartService.getCartItems(page, size, mockUser);

		//then
		assertThat(result.get(0).getId()).isEqualTo(mockCartItemId1);
		assertThat(result.get(1).getId()).isEqualTo(mockCartItemId2);
	}

}

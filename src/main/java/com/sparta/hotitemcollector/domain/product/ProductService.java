package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private UserService userService;
    private ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        /*User user1 = userService.findById(user.getId());*/

        Product product = Product.builder()
            .requestDto(requestDto)
            .user(user)
            .build();

        productRepository.save(product);
        ProductResponseDto responseDto = new ProductResponseDto(product);
        return responseDto;
    }
}

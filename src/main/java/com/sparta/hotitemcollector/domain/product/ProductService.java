package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
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

    public ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto,
        User user) {
        Product product = findById(productId);

        if (!product.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        if (product.getStatus().equals(ProductStatus.SOLD_OUT)) {
            throw new CustomException(ErrorCode.ALREADY_SOLD_OUT);
        }

        product.updateProduct(requestDto);
        productRepository.save(product);
        ProductResponseDto responseDto = new ProductResponseDto(product);
        return responseDto;
    }

    public void deleteProduct(Long productId, User user) {
        Product product = findById(productId);

        if (!product.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        productRepository.delete(product);
    }

    public ProductResponseDto getProduct(Long productId) {
        Product product = findById(productId);
        ProductResponseDto responseDto = new ProductResponseDto(product);
        return responseDto;
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new CustomException(ErrorCode.NON_EXISTENT_PRODUCT)
        );
    }
}

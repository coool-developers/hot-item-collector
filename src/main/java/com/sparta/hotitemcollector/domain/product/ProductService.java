package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.follow.Follow;
import com.sparta.hotitemcollector.domain.follow.FollowService;
import com.sparta.hotitemcollector.domain.like.LikeService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private UserService userService;
    private ProductRepository productRepository;
    private FollowService followService;
    private LikeService likeService;

    @Transactional
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

    @Transactional
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

    @Transactional
    public void deleteProduct(Long productId, User user) {
        Product product = findById(productId);

        if (!product.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        Product product = findById(productId);
        ProductResponseDto responseDto = new ProductResponseDto(product);
        return responseDto;
    }

    public List<ProductSimpleResponseDto> getFollowProduct(User user) {
        List<Follow> followList = followService.getAllFollowers(user);

        List<User> followingUsers = followList.stream()
            .map(Follow::getFollowing)
            .collect(Collectors.toList());

        List<Product> productList = productRepository.findByUserIn(followingUsers);

        return productList.stream()
            .map(ProductSimpleResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<ProductSimpleResponseDto> getLikeProduct(User user) {

    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );
    }
}

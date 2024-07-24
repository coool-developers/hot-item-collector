package com.sparta.hotitemcollector.domain.product.service;

import com.sparta.hotitemcollector.domain.follow.Follow;
import com.sparta.hotitemcollector.domain.follow.FollowService;
import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import com.sparta.hotitemcollector.domain.product.repository.ProductImageRepository;
import com.sparta.hotitemcollector.domain.product.repository.ProductRepository;
import com.sparta.hotitemcollector.domain.product.dto.HotProductResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductRequestDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductSimpleResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FollowService followService;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {

        Product product = Product.builder()
            .requestDto(requestDto)
            .user(user)
            .build();

        productRepository.save(product);

        // ProductImageDto를 ProductImage 엔티티로 변환하여 저장
        List<ProductImage> productImages = requestDto.getImages().stream()
            .map(dto -> new ProductImage(dto.getFilename(), dto.getImageUrl(), product, user))
            .collect(Collectors.toList());

        // 각 이미지 엔티티를 저장
        for (ProductImage productImage : productImages) {
            productImageRepository.save(productImage);
        }

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

    @Transactional(readOnly = true)
    public List<ProductSimpleResponseDto> getFollowProduct(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Follow> followList = followService.getAllFollowers(user);

        List<User> followingUsers = followList.stream()
            .map(Follow::getFollowing)
            .collect(Collectors.toList());

        Page<Product> productPage = productRepository.findByUserIn(followingUsers, pageable);

        return productPage.getContent()
            .stream()
            .map(ProductSimpleResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<HotProductResponseDto> getHotProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage = productRepository.findTop10ByOrderByLikesDesc(pageable);

        return productPage.getContent()
            .stream()
            .map(HotProductResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductSimpleResponseDto> getSaleProduct(User user, ProductStatus status, int page,
        int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage = productRepository.findByUserAndStatus(user, status, pageable);

        return productPage.getContent()
            .stream()
            .map(ProductSimpleResponseDto::new)
            .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );
    }

    public void increaseLikes(Long productId) {
        Product product = findById(productId);
        product.increaseLikes();
        productRepository.save(product);
    }

    public void decreaseLikes(Long productId) {
        Product product = findById(productId);
        product.decreaseLikes();
        productRepository.save(product);
    }
}

package com.sparta.hotitemcollector.domain.product.service;

import com.sparta.hotitemcollector.domain.follow.Follow;
import com.sparta.hotitemcollector.domain.follow.FollowService;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageDto;
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
import java.util.ArrayList;
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


        ProductResponseDto responseDto = new ProductResponseDto(product,requestDto.getImages());
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

        // 5. 기존 이미지를 삭제하고 새로운 이미지를 추가합니다.
        List<ProductImage> existingImages = product.getImages();
        List<ProductImageDto> newImageDtos = requestDto.getImages();

        // 삭제할 이미지들
        List<ProductImage> imagesToRemove = new ArrayList<>(existingImages);

        for (ProductImageDto newImageDto : newImageDtos) {
            boolean imageExists = existingImages.stream()
                .anyMatch(image -> image.getImageUrl().equals(newImageDto.getImageUrl()));

            if (!imageExists) {
                ProductImage newImage = new ProductImage();
                newImage.updateProductImage(newImageDto);
                productImageRepository.save(newImage);
            }
            imagesToRemove.removeIf(image -> image.getImageUrl().equals(newImageDto.getImageUrl()));
        }

        // 삭제된 이미지들을 DB에서 제거합니다.
        if (!imagesToRemove.isEmpty()) {
            productImageRepository.deleteAll(imagesToRemove);
        }

        // 6. 업데이트된 제품 정보를 저장합니다.
        productRepository.save(product);

        // 7. ProductResponseDto를 생성하여 반환합니다.
        return new ProductResponseDto(product,
            newImageDtos.stream()
                .map(dto -> new ProductImageDto(dto.getFilename(), dto.getImageUrl()))
                .collect(Collectors.toList()));
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
        // Product와 관련된 이미지 정보를 가져옴
        Product product = productRepository.findByIdWithImages(productId);

        // Product와 연관된 이미지 정보를 DTO로 변환
        List<ProductImageDto> imageDtos = product.getImages().stream()
            .map(image -> new ProductImageDto(image.getFilename(), image.getImageUrl()))
            .collect(Collectors.toList());

        // ProductResponseDto 생성
        ProductResponseDto responseDto = new ProductResponseDto(product, imageDtos);
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

package com.sparta.hotitemcollector.domain.product.service;

import com.sparta.hotitemcollector.domain.follow.Follow;
import com.sparta.hotitemcollector.domain.follow.FollowService;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageRequestDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.ProductImage;
import com.sparta.hotitemcollector.domain.product.repository.ProductImageRepository;
import com.sparta.hotitemcollector.domain.product.repository.ProductRepository;
import com.sparta.hotitemcollector.domain.product.dto.HotProductResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductRequestDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductSimpleResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import com.sparta.hotitemcollector.domain.s3.service.S3Service;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileImageResponseDto;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final S3Service s3Service;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {

        // Product 엔티티 생성
        Product product = Product.builder()
            .requestDto(requestDto)
            .user(user)
            .build();

        // Product 엔티티 저장
        productRepository.save(product);

        // ProductImage 엔티티 생성 및 저장
        List<ProductImage> productImages = requestDto.getImages().stream()
            .map(dto -> new ProductImage(dto.getFilename(), dto.getImageUrl(), product, user))
            .collect(Collectors.toList());

        // 각 이미지 엔티티를 저장
        for (ProductImage productImage : productImages) {
            productImageRepository.save(productImage);
        }

        // Product 엔티티에서 저장된 이미지를 가져옴
        List<ProductImageResponseDto> imageDtos = productImages.stream()
            .map(image -> new ProductImageResponseDto(image))
            .collect(Collectors.toList());

        ProfileImageResponseDto profileImageDto = new ProfileImageResponseDto(product.getUser()
            .getProfileImage());

        // ProductResponseDto 생성
        ProductResponseDto responseDto = new ProductResponseDto(product, imageDtos,profileImageDto);
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

        // 기존 이미지 삭제하고 새로운 이미지 추가
        List<ProductImage> existingImages = product.getImages();
        List<ProductImageRequestDto> newImageDtos = requestDto.getImages();

        // 기존 이미지 모두 삭제
        for (ProductImage imageToRemove : existingImages) {
            s3Service.deleteImage(imageToRemove.getFilename());
            productImageRepository.delete(imageToRemove);
        }
        product.getImages().clear(); // 부모 엔티티에서 자식 엔티티를 제거

        // 새로운 이미지 추가
        for (ProductImageRequestDto newImageDto : newImageDtos) {
            ProductImage newImage = new ProductImage(newImageDto.getFilename(),
                newImageDto.getImageUrl(), product, user);
            product.addImage(newImage); // 양방향 연관관계 설정
            productImageRepository.save(newImage);
        }

        productRepository.save(product);

        // 제품의 모든 이미지를 다시 조회하여 반환
        List<ProductImageResponseDto> updatedImageDtos = product.getImages().stream()
            .map(ProductImageResponseDto::new)
            .collect(Collectors.toList());

        ProfileImageResponseDto profileImageResponseDto = new ProfileImageResponseDto(product.getUser()
            .getProfileImage());

        // ProductResponseDto 생성 및 반환
        return new ProductResponseDto(product, updatedImageDtos,profileImageResponseDto);
    }


    @Transactional
    public void deleteProduct(Long productId, User user) {
        Product product = findById(productId);

        if (!product.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        // 제품과 연결된 모든 이미지를 S3에서 삭제
        List<ProductImage> images = product.getImages();
        for (ProductImage image : images) {
            s3Service.deleteImage(image.getFilename());
        }

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        // Product와 관련된 이미지 정보를 가져옴
        Product product = productRepository.findByIdWithImages(productId);

        // Product와 연관된 이미지 정보를 DTO로 변환
        List<ProductImageResponseDto> imageDtos = product.getImages().stream()
            .map(image -> new ProductImageResponseDto(image))
            .collect(Collectors.toList());

        // ProfileImageResponseDto를 생성, 프로필 이미지가 없으면 null 설정
        ProfileImageResponseDto profileImageResponseDto = product.getUser().getProfileImage() != null
            ? new ProfileImageResponseDto(product.getUser().getProfileImage())
            : null;

        // ProductResponseDto 생성
        ProductResponseDto responseDto = new ProductResponseDto(product, imageDtos,profileImageResponseDto);
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
        Page<Product> productPage=Page.empty(pageable);

        if(status!=null){
            productPage = productRepository.findByUserAndStatus(user, status, pageable);
        }
        if(status==null){
            productPage = productRepository.findByUser(user,pageable);
        }

        return productPage.getContent()
            .stream()
            .map(ProductSimpleResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<ProductSimpleResponseDto> getNewProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> productPage = productRepository.findAll(pageable);

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

    public void updateStatus(Long productId) {
        Product product = findById(productId);
        product.updateStatus();
        productRepository.save(product);
    }
    public List<Product> findByUserAndStatus(User user,ProductStatus status){
        return productRepository.findByUserAndStatus(user,status);
    }
}

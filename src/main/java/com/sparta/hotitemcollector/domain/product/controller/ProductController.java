package com.sparta.hotitemcollector.domain.product.controller;

import com.sparta.hotitemcollector.domain.product.dto.HotProductResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductImageRequestDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductRequestDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductResponseDto;
import com.sparta.hotitemcollector.domain.product.dto.ProductSimpleResponseDto;
import com.sparta.hotitemcollector.domain.product.entity.Product;
import com.sparta.hotitemcollector.domain.product.entity.ProductCategory;
import com.sparta.hotitemcollector.domain.product.entity.ProductStatus;
import com.sparta.hotitemcollector.domain.product.service.ProductImageService;
import com.sparta.hotitemcollector.domain.product.service.ProductService;
import com.sparta.hotitemcollector.domain.product.service.SearchService;
import com.sparta.hotitemcollector.domain.s3.service.S3Service;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;
    private final S3Service s3Service;
    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponseDto>> createProduct(
        @Valid @RequestPart("requestDto") ProductRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestPart("files") List<MultipartFile> files) throws IOException {

        // 크기제한, 확장자 확인
        for (MultipartFile file : files) {
            productImageService.validateFile(file);
        }
        // S3에 파일 업로드
        List<ProductImageRequestDto> images = s3Service.uploadFiles(files);

        requestDto.addImages(images);
        ProductResponseDto responseDto = productService.createProduct(requestDto,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("상품 등록 성공", 201, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponseDto>> updateProduct(
        @PathVariable(name = "productId") Long productId,
        @Valid @RequestPart("requestDto") ProductRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestPart(value = "files", required = false) List<MultipartFile> files)
        throws IOException {

        // 현재 제품 정보를 조회
        Product product = productService.findById(productId);

        // 사용자 권한 확인
        if (!product.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_SAME_USER);
        }

        // 크기제한, 확장자 확인
        for (MultipartFile file : files) {
            productImageService.validateFile(file);
        }
        // S3에 파일 업로드
        List<ProductImageRequestDto> images = s3Service.uploadFiles(files);

        requestDto.addImages(images);

        // 제품 업데이트
        ProductResponseDto responseDto = productService.updateProduct(productId, requestDto,
            userDetails.getUser());

        // 응답 반환
        CommonResponse<ProductResponseDto> response = new CommonResponse<>("상품 수정 성공", 200,
            responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse> deleteProduct(
        @PathVariable(name = "productId") Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.deleteProduct(productId, userDetails.getUser());
        CommonResponse response = new CommonResponse("상품 삭제 성공", 204, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponseDto>> getProduct(
        @PathVariable(name = "productId") Long productId) {
        ProductResponseDto responseDto = productService.getProduct(productId);
        CommonResponse response = new CommonResponse("상품 단건 조회 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/follow")
    public ResponseEntity<CommonResponse<List<ProductSimpleResponseDto>>> getFollowProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductSimpleResponseDto> responseDtoList = productService.getFollowProduct(
            userDetails.getUser(), page - 1, size);
        CommonResponse<List<ProductSimpleResponseDto>> response = new CommonResponse<>(
            "팔로우한 사용자의 상품 목록 조회 성공", 200, responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/hot")
    public ResponseEntity<CommonResponse<List<HotProductResponseDto>>> getHotProduct(
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<HotProductResponseDto> responseDtoList = productService.getHotProduct(page - 1, size);
        CommonResponse<List<HotProductResponseDto>> response = new CommonResponse<>(
            "Hot Top 10 조회 성공", 200, responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/sale")
    public ResponseEntity<CommonResponse<List<ProductSimpleResponseDto>>> getSaleProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam ProductStatus status,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductSimpleResponseDto> responseDtoList = productService.getSaleProduct(
            userDetails.getUser(), status, page - 1, size);
        CommonResponse<List<ProductSimpleResponseDto>> response = new CommonResponse<>(
            "판매 상태에 따른 상품 목록 조회 성공", 200, responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ProductSimpleResponseDto>>> getSearchProduct(
        @RequestParam(required = false) String nickname,
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) ProductCategory category,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductSimpleResponseDto> responseDtoList = searchService.getSearchProduct(
            nickname, productName, category, page - 1, size);
        CommonResponse<List<ProductSimpleResponseDto>> response = new CommonResponse<>(
            "검색을 통한 상품 목록 조회 성공", 200, responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

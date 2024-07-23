package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponseDto>> createProduct(
        @Valid @RequestBody ProductRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponseDto responseDto = productService.createProduct(requestDto,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("상품 등록 성공", 201, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponseDto>> updateProduct(
        @PathVariable(name = "productId") Long productId,
        @Valid @RequestBody ProductRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponseDto responseDto = productService.updateProduct(productId, requestDto,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("상품 수정 성공", 200, responseDto);
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
        @RequestParam(required=false) String nickname,
        @RequestParam(required=false) String productName,
        @RequestParam(required=false) ProductCategory category,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductSimpleResponseDto> responseDtoList = searchService.getSearchProduct(
            nickname,productName,category,page-1,size);
        CommonResponse<List<ProductSimpleResponseDto>> response = new CommonResponse<>(
            "검색을 통한 상품 목록 조회 성공", 200, responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

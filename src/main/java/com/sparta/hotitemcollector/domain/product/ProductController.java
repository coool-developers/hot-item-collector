package com.sparta.hotitemcollector.domain.product;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponseDto>> createProduct(
        @RequestBody ProductRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponseDto responseDto = productService.createProduct(requestDto,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("상품 등록 성공", 201, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponseDto>> updateProduct(@PathVariable(name = "productId") Long productId, @RequestBody ProductRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        ProductResponseDto responseDto = productService.updateProduct(productId,requestDto,
            userDetails.getUser());
        CommonResponse response = new CommonResponse("상품 수정 성공", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

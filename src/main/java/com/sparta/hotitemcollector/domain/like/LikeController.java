package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.product.dto.ProductSimpleResponseDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes/{productId}")
    public ResponseEntity<CommonResponse> createLike(@PathVariable("productId") Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // User를 받아오기
        User user = userDetails.getUser();

        // 서비스에서 좋아요 처리 로직 수행
        boolean isLiked = likeService.createOrDeleteLike(productId, user);

        if (isLiked) {
            CommonResponse response = new CommonResponse<>("좋아요 생성 완료", 201, "");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            CommonResponse response = new CommonResponse<>("좋아요 취소 완료", 200, "");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // 로그인한 유저의 상품 좋아요 여부를 확인하는 메소드
    @GetMapping("/likes/{productId}")
    public ResponseEntity<CommonResponse<UserLikeResponseDto>> getUserLike(@PathVariable("productId") Long productId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        UserLikeResponseDto responseDto = likeService.getUserLike(productId, user);
        CommonResponse response = new CommonResponse<>("로그인한 유저의 상품 좋아요 여부 확인 완료", 200, responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products/like")
    public ResponseEntity<CommonResponse<List<ProductSimpleResponseDto>>> getLikeProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<ProductSimpleResponseDto> responseDtoList = likeService.getLikeProduct(
            userDetails.getUser(), page - 1, size);
        CommonResponse<List<ProductSimpleResponseDto>> response = new CommonResponse<>(
            "좋아요한 상품 목록 조회 성공", 200, responseDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

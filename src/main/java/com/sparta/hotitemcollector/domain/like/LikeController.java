package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/likes/{productId}")
    public ResponseEntity<CommonResponse> createLike(@PathVariable ("productId") Long productId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
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
}

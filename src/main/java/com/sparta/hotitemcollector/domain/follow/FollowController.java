package com.sparta.hotitemcollector.domain.follow;

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
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<CommonResponse> createFollow(@PathVariable ("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.createFollow(userId, userDetails.getUser());

        CommonResponse response = new CommonResponse<>("팔로우 신청 완료", 200, "");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

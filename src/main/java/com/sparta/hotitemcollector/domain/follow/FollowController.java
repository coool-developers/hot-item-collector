package com.sparta.hotitemcollector.domain.follow;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<CommonResponse> createFollow(@PathVariable ("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.createFollow(userId, userDetails.getUser());

        CommonResponse response = new CommonResponse<>("팔로우 신청 완료", 201, "");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/follow/{userId}")
    public ResponseEntity<CommonResponse> deleteFollow(@PathVariable ("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.deleteFollow(userId, userDetails.getUser());

        CommonResponse response = new CommonResponse<>("팔로우 취소 완료", 200, "");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/follows")
    public ResponseEntity<CommonResponse> getFollows(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<GetAllFollowsResponseDto> followingList = followService.getFollows(userDetails.getUser());

        CommonResponse response = new CommonResponse<>("팔로우 목록 조회 성공", 200, followingList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

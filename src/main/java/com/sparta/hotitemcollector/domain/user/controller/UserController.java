package com.sparta.hotitemcollector.domain.user.controller;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.dto.user.UserAddressDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.user.GetUserProfileDto;
import com.sparta.hotitemcollector.domain.user.dto.user.updatePasswordRequestDto;
import com.sparta.hotitemcollector.domain.user.service.UserService;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PatchMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody ProfileRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto responseDto = userService.updateProfile(requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("회원 정보 수정 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<CommonResponse> updatePassword(@RequestBody updatePasswordRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("비밀번호 수정 성공",200,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<CommonResponse> getUserProfile(@PathVariable Long userId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        GetUserProfileDto profile = userService.getUserProfile(userId, Optional.ofNullable(userDetails));
        CommonResponse response = new CommonResponse<>("유저 프로필 조회 성공", 200, profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile/address")
    public ResponseEntity<CommonResponse> getUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserAddressDto profile = userService.getUserAddress(userDetails.getUser());
        CommonResponse response = new CommonResponse<>("유저 주소 조회 성공", 200, profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

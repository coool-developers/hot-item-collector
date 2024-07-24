package com.sparta.hotitemcollector.domain.user.controller;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileResponseDto;
import com.sparta.hotitemcollector.domain.user.service.UserService;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

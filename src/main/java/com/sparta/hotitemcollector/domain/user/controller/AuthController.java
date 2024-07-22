package com.sparta.hotitemcollector.domain.user.controller;

import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.domain.user.dto.SignupRequestDto;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        CommonResponse response = new CommonResponse<>("회원가입 완료",201,"");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}

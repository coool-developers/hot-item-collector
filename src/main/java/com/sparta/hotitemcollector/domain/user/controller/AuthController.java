package com.sparta.hotitemcollector.domain.user.controller;

import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.domain.user.dto.LoginReqeustDto;
import com.sparta.hotitemcollector.domain.user.dto.LoginResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.SignupRequestDto;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        CommonResponse response = new CommonResponse<>("회원가입 성공",201,"");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody LoginReqeustDto requestDto) {
        LoginResponseDto responseDto = userService.login(requestDto);
        CommonResponse response = new CommonResponse<>("로그인 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        CommonResponse response = new CommonResponse<>("로그아웃 성공",200,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}

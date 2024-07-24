package com.sparta.hotitemcollector.domain.user.controller;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.service.AuthService;
import com.sparta.hotitemcollector.domain.user.dto.auth.LoginReqeustDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.LoginResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.RefreshRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.SignupRequestDto;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
        CommonResponse response = new CommonResponse<>("회원가입 성공",201,"");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody LoginReqeustDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        CommonResponse response = new CommonResponse<>("로그인 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(@RequestHeader("Authorization") String accessToken,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(accessToken,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("로그아웃 성공",200,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<CommonResponse> withdraw(@RequestHeader("Authorization") String accessToken,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.withdraw(accessToken,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("회원탈퇴 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse> refreshToken(@RequestBody RefreshRequestDto refreshRequestDto) {
        LoginResponseDto responseDto = authService.refreshToken(refreshRequestDto);

        CommonResponse response = new CommonResponse<>("access 토큰 재발급 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

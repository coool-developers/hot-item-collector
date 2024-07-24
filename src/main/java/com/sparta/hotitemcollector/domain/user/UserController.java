package com.sparta.hotitemcollector.domain.user;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.dto.auth.LoginReqeustDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.LoginResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.RefreshRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.SignupRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.UserAddressDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.user.GetUserProfileDto;
import com.sparta.hotitemcollector.domain.user.dto.user.updatePasswordRequestDto;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import jakarta.validation.Valid;
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

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody SignupRequestDto requestDto) {
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
    public ResponseEntity<CommonResponse> logout(@RequestHeader("Authorization") String accessToken,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(accessToken,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("로그아웃 성공",200,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<CommonResponse> withdraw(@RequestHeader("Authorization") String accessToken,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(accessToken,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("회원탈퇴 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse> refreshToken(@RequestBody RefreshRequestDto refreshRequestDto) {
        LoginResponseDto responseDto = userService.refreshToken(refreshRequestDto);

        CommonResponse response = new CommonResponse<>("access 토큰 재발급 성공",204,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody ProfileRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto responseDto = userService.updateProfile(requestDto,userDetails.getUser());
        CommonResponse response = new CommonResponse<>("회원 정보 수정 성공",200,responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<CommonResponse> updatePassword(@Valid @RequestBody updatePasswordRequestDto requestDto,
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

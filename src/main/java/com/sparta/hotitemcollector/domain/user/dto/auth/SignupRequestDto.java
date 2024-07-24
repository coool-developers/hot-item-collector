package com.sparta.hotitemcollector.domain.user.dto.auth;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String loginId;
    private String password;
    private String username;
    private String nickname;
}

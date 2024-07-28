package com.sparta.hotitemcollector.domain.user.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String access;
    private String refresh;
}

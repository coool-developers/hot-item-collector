package com.sparta.hotitemcollector.domain.user.dto.auth;

import lombok.Getter;

@Getter
public class LoginReqeustDto {
    private String loginId;
    private String password;
}

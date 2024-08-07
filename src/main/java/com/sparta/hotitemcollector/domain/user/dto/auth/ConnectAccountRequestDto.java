package com.sparta.hotitemcollector.domain.user.dto.auth;

import lombok.Getter;

@Getter
public class ConnectAccountRequestDto {
    private Long oauthId;
    private String socialId;
    private String loginId;
    private String password;
}

package com.sparta.hotitemcollector.domain.user.dto.user;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String nickname;
    private String phoneNumber;
    private String address;
    private String info;
    private String profileImage;
}

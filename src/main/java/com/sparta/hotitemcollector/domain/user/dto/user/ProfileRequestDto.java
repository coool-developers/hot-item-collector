package com.sparta.hotitemcollector.domain.user.dto.user;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String nickname;
    private String phoneNumber;
    private String address;
    private String info;
    private ProfileImageRequestDto profileImage;

    public void addImage(ProfileImageRequestDto image) {
        this.profileImage=image;
    }
}

package com.sparta.hotitemcollector.domain.user.dto.user;

import com.sparta.hotitemcollector.domain.user.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private Long id;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String info;
    private String profileImage;

    public ProfileResponseDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.info = user.getInfo();
        this.profileImage = user.getProfileImage();
    }
}



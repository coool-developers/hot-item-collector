package com.sparta.hotitemcollector.domain.user.dto.user;

import com.sparta.hotitemcollector.domain.user.User;
import lombok.Getter;

@Getter
public class GetUserProfileDto {
    private Long id;
    private String nickname;
    private String info;
    private String profileImage;
    private boolean isOwnProfile;

    public GetUserProfileDto(User user, boolean isOwnProfile) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.info = user.getInfo();
        this.profileImage = user.getProfileImage();
        this.isOwnProfile = isOwnProfile;
    }
}

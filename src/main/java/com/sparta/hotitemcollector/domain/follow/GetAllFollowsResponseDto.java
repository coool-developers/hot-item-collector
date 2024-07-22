package com.sparta.hotitemcollector.domain.follow;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetAllFollowsResponseDto {
    private Long userId;
    private String profileImage;
    private String profileInfo;
    private String userName;

    public GetAllFollowsResponseDto(Long userId, String profileImage, String profileInfo, String userName) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.profileInfo = profileInfo;
        this.userName = userName;
    }
}

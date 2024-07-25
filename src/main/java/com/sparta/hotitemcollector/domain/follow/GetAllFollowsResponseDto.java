package com.sparta.hotitemcollector.domain.follow;

import com.sparta.hotitemcollector.domain.user.dto.user.ProfileImageResponseDto;
import lombok.Getter;

@Getter
public class GetAllFollowsResponseDto {
    private Long userId;
    private ProfileImageResponseDto profileImage;
    private String profileInfo;
    private String userName;


    public GetAllFollowsResponseDto(Long userId, ProfileImageResponseDto profileImage, String profileInfo, String userName) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.profileInfo = profileInfo;
        this.userName = userName;
    }
}

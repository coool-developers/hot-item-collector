package com.sparta.hotitemcollector.domain.user.dto.user;

import com.sparta.hotitemcollector.domain.user.User;
import lombok.Getter;
import org.hibernate.sql.model.ast.builder.TableInsertBuilderStandard;

@Getter
public class GetMyProfileDto {
    private Long id;
    private String username;
    private String loginId;
    private String nickname;
    private String info;
    private ProfileImageResponseDto profileImage;
    private String phoneNumber;
    private String address;
    private Long followerCount;

    public GetMyProfileDto(User user, ProfileImageResponseDto profileImageResponseDto,Long followerCount) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.info = user.getInfo();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImage = profileImageResponseDto;
        this.followerCount=followerCount;
    }
}

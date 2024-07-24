package com.sparta.hotitemcollector.domain.user.dto.user;

import lombok.Getter;

@Getter
public class updatePasswordRequestDto {
    private String oldPassword;
    private String newPassword;
}

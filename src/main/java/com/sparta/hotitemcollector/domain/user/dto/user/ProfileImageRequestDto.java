package com.sparta.hotitemcollector.domain.user.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProfileImageRequestDto {
    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String filename;
    @NotBlank(message = "이미지 URL을 입력해주세요.")
    private String imageUrl;

}

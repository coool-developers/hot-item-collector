package com.sparta.hotitemcollector.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String access;
    private String refresh;
}

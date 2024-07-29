package com.sparta.hotitemcollector.domain.user.dto.user;

import com.sparta.hotitemcollector.domain.user.User;
import lombok.Getter;

@Getter
public class UserAddressDto {
    private String phoneNumber;
    private String address;
    private String username;

    public UserAddressDto(User user) {
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.username = user.getUsername();
    }
}

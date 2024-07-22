package com.sparta.hotitemcollector.domain.user;

import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "login_id",nullable = false)
    private String loginId;

    @Column
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRole role;

    @Column
    private String address;

    @Column
    private String profileImage;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private UserStatus userStatus;

    @Column
    private String info;

    @Builder
    public User(String loginId,String password, String username, String nickname){
        this.userStatus = UserStatus.NORMAL;
        this.role = UserRole.USER;
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
    }
}

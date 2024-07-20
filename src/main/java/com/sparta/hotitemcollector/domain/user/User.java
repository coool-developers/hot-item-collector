package com.sparta.hotitemcollector.domain.user;

import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "user_name",nullable = false)
    private String userName;

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
    public User(){
        this.userStatus = UserStatus.NORMAL;
    }
}

package com.sparta.hotitemcollector.domain.token;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@Table(name = "token")
@NoArgsConstructor
public class Token extends Timestamped {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @OneToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (name = "refresh_time", nullable = false)
    private Date refreshTime;

    @Column (name = "refresh_token", nullable = false)
    private String refreshToken;

    @Builder
    public Token(long id, User user, Date refreshTime, String refreshToken) {
        this.id = id;
        this.user = user;
        this.refreshTime = refreshTime;
        this.refreshToken = refreshToken;
    }
}

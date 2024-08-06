package com.sparta.hotitemcollector.domain.token;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Setter
    @Column (name = "refresh_time", nullable = false)
    private Date refreshTime;

    @Setter
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

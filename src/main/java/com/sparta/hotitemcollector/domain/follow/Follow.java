package com.sparta.hotitemcollector.domain.follow;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Follow extends Timestamped {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Builder
    public Follow (User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}

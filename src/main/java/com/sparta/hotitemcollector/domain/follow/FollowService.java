package com.sparta.hotitemcollector.domain.follow;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void createFollow(Long userId, User followerUser) {
        // 팔로우 당하는 사람 ERD에서 following_id
        User followingUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Follow newFollow = Follow.builder()
                .follower(followerUser)
                .following(followingUser)
                .build();

        if (checkFollowAlready(followerUser, followingUser)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_FOLLOW);
        }
        followRepository.save(newFollow);
    }

    public boolean checkFollowAlready(User followerUser, User followingUser) {
        return followRepository.existsFollowerAndFollowing(followerUser, followingUser);
    }

}

package com.sparta.hotitemcollector.domain.follow;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void deleteFollow(Long userId, User followerUser) {
        User followingUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!checkFollowAlready(followerUser, followingUser)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_FOLLOW);
        }

        Follow followForDelete = checkFollowExists(followerUser, followingUser);

        followRepository.delete(followForDelete);
    }

    @Transactional(readOnly = true)
    public List<GetAllFollowsResponseDto> getFollows(User user) {
        List<Follow> followList = getAllFollowers(user);

        return followList.stream()
                .map(follow -> new GetAllFollowsResponseDto(
                        follow.getFollowing().getId(),
                        follow.getFollowing().getProfileImage(),
                        follow.getFollowing().getInfo(),
                        follow.getFollowing().getNickname()
                ))
                .toList();
    }

    public boolean checkFollowAlready(User followerUser, User followingUser) {
        return followRepository.existsByFollowerIdAndFollowingId(followerUser.getId(), followingUser.getId());
    }

    public Follow checkFollowExists(User followerUser, User followingUser) {
        return followRepository.findByFollowerIdAndFollowingId(followerUser.getId(), followingUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FOLLOW));
    }

    public List<Follow> getAllFollowers(User user) {
        return followRepository.findByFollowerId(user.getId());
    }
}

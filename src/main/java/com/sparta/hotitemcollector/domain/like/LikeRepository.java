package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByProductIdAndUser(Long productId, User user);

    List<Likes> findByUser(User user);
}

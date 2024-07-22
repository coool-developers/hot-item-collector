package com.sparta.hotitemcollector.domain.like;

import com.sparta.hotitemcollector.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByProductIdAndUser(Long productId, User user);
}

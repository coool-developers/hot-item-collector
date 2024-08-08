package com.sparta.hotitemcollector.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    List<User> findByNicknameContainingIgnoreCase(String nickname);

    boolean existsByNicknameContainingIgnoreCase(String nickname);

    Optional<User> findByNickname(String nickname);
}

package com.sparta.hotitemcollector.domain.token;

import com.sparta.hotitemcollector.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);
}

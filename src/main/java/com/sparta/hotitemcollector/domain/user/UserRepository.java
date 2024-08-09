package com.sparta.hotitemcollector.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    @Query("SELECT u "
        + "FROM User u JOIN FETCH u.profileImage pi "
        + "WHERE upper(u.nickname) LIKE upper (CONCAT (:nickname,'%')) escape '\\'")
    List<User> findByNicknameContainingIgnoreCase(String nickname);

    boolean existsByNicknameContainingIgnoreCase(String nickname);

    Optional<User> findByNickname(String nickname);
}

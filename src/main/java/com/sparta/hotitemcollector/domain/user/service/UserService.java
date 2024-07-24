package com.sparta.hotitemcollector.domain.user.service;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 닉네임으로 유저 리스트 검색
     * @param nickname
     * @return
     */
    public List<User> findByNicknameContainingIgnoreCase (String nickname) {
        return userRepository.findByNicknameContainingIgnoreCase(nickname);
    }
}

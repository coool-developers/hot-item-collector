package com.sparta.hotitemcollector.domain.user;

import com.sparta.hotitemcollector.domain.user.dto.SignupRequestDto;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public void signup(SignupRequestDto requestDto) {
        Optional<User> invalidUser = userRepository.findByLoginId(requestDto.getLoginId());

        if (invalidUser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        String password = requestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .loginId(requestDto.getLoginId())
                .password(encodedPassword)
                .username(requestDto.getUsername())
                .nickname(requestDto.getNickname())
                .build();

        userRepository.save(user);
    }

}

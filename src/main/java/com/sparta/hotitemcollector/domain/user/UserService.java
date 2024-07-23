package com.sparta.hotitemcollector.domain.user;

import com.sparta.hotitemcollector.domain.token.Token;
import com.sparta.hotitemcollector.domain.token.TokenService;
import com.sparta.hotitemcollector.domain.user.dto.LoginReqeustDto;
import com.sparta.hotitemcollector.domain.user.dto.LoginResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.SignupRequestDto;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import com.sparta.hotitemcollector.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserService 로그")
public class UserService {

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;


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

    public LoginResponseDto login(LoginReqeustDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        String access = jwtUtil.createAccessToken(user.getLoginId(),user.getRole());
        String refresh = jwtUtil.createRefreshToken(user.getLoginId(),user.getRole());


        // 토큰을 데이터베이스에 저장
        Optional<Token> optionalToken = tokenService.checkToken(user);
        if(optionalToken.isPresent()) {
            Token token = optionalToken.get();
            tokenService.updateToken(token,refresh);
        }else{
            tokenService.saveToken(user, refresh);
        }

        return new LoginResponseDto("Bearer "+access, refresh);
    }

    public void logout(String token) {
        try {
            String tokenValue = jwtUtil.substringToken(token);

            // 토큰 유효한지 확인 - 로그아웃유무 토큰이 유효한지
            jwtUtil.validateToken(tokenValue);

            // 블랙리스트에 토큰 추가
            jwtUtil.createblacklistToken(tokenValue);
            if(!jwtUtil.isTokenBlacklisted(tokenValue)) {
                throw new IllegalStateException("Failed to blacklist token");
            }
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to logout");
        }
    }

}

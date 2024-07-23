package com.sparta.hotitemcollector.domain.token;

import com.sparta.hotitemcollector.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token saveToken(User user, String refreshToken) {
        Token token = Token.builder()
                .user(user)
                .refreshToken(refreshToken)
                .refreshTime(new Date())
                .build();

        return tokenRepository.save(token);
    }

    // 사용자와 관련된 토큰이 존재하는지 확인
    public Optional<Token> checkToken(User user) {
        return tokenRepository.findByUser(user);
    }


    // 사용자와 관련된 기존 토큰 업데이트
    public void updateToken(Token token, String newRefreshToken) {
        token.setRefreshToken(newRefreshToken);
        token.setRefreshTime(new Date());
        tokenRepository.save(token); // 토큰 업데이트
    }
}

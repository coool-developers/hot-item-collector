package com.sparta.hotitemcollector.domain.token;

import com.sparta.hotitemcollector.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

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



}

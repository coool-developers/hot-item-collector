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

    /**
     * refreshToken을 DB에 저장
     * @param user
     * @param refreshToken
     * @return
     */
    public Token saveToken(User user, String refreshToken) {
        Token token = Token.builder()
                .user(user)
                .refreshToken(refreshToken)
                .refreshTime(new Date())
                .build();

        return tokenRepository.save(token);
    }

    /**
     *  user와 관련된 토큰이 존재하는지 확인
     * @param user
     * @return
     */
    public Optional<Token> findRefreshToken(User user) {
        return tokenRepository.findByUser(user);
    }


    /**
     * 사용자와 관련된 기존 토큰 업데이트
     * @param token
     * @param newRefreshToken
     */
    public void updateToken(Token token, String newRefreshToken) {
        token.setRefreshToken(newRefreshToken);
        token.setRefreshTime(new Date());
        tokenRepository.save(token); // 토큰 업데이트
    }

    /**
     * 토큰 삭제
     * @param token
     */
    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }
}

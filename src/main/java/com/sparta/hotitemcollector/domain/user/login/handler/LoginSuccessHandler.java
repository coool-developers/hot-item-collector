package com.sparta.hotitemcollector.domain.user.login.handler;

import com.sparta.hotitemcollector.domain.token.TokenService;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String loginId = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        String accessToken = jwtUtil.createAccessToken(loginId); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = jwtUtil.createRefreshToken(loginId); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

        // 응답 헤더에 AccessToken, RefreshToken 실어서 응답
        jwtUtil.sendAccessAndRefreshToken(response,accessToken,refreshToken);

        // Update or save refresh token in the database
        userRepository.findByLoginId(loginId).ifPresent(user -> {
            tokenService.findRefreshToken(user).ifPresentOrElse(
                    token -> tokenService.updateToken(token, refreshToken),
                    () -> tokenService.saveToken(user, refreshToken)
            );
            log.info("User found and token updated/saved");
        });
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
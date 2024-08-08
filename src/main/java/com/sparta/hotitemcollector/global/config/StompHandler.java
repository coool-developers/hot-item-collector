package com.sparta.hotitemcollector.global.config;

import com.sparta.hotitemcollector.domain.security.UserDetailsServiceImpl;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import com.sparta.hotitemcollector.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // CONNECT 명령 시 토큰 검증
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith(JwtUtil.BEARER_PREFIX)) {
                token = jwtUtil.substringToken(token); // "Bearer " 접두사 제거
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getLoginIdFromToken(token);
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                    log.info(token + " - " + userDetails.getUsername());

                    // Authentication 객체 생성
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // 현재 인증된 사용자를 설정
                    accessor.setUser(authentication); // Authentication 객체를 설정
                } else {
                    throw new CustomException(ErrorCode.INVALID_TOKEN);
                }
            } else {
                throw new CustomException(ErrorCode.HEADER_NOT_FOUND);
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            // 구독 시 수행할 작업
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            // 메시지 전송 시 수행할 작업
        }

        return message;
    }
}
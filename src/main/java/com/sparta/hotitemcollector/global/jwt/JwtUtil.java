package com.sparta.hotitemcollector.global.jwt;

import com.sparta.hotitemcollector.domain.user.UserRole;
import com.sparta.hotitemcollector.global.config.JwtConfig;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";

    // 사용하는 암호화 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final long tokenExpiration;
    private final long refreshTokenExpiration;
    private final SecretKey secretKey;
    private final RedisUtil redisUtil;

    public JwtUtil(JwtConfig jwtConfig, RedisUtil redisUtil) {
        this.tokenExpiration = jwtConfig.getTokenExpiration();
        this.refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
        this.redisUtil = redisUtil;
    }

    public String createAccessToken(String userId, UserRole role) {
        return createToken(userId, role, tokenExpiration);
    }

    public String createRefreshToken(String userId,UserRole role) {
        return createToken(userId, role, refreshTokenExpiration);
    }


    // 토큰 생성
    public String createToken(String userId, UserRole role, long expiration) {
        return Jwts.builder()
                .setSubject(userId) // 토큰 발행 주체
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(new Date()) // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료 시간
                .signWith(secretKey, signatureAlgorithm)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.FALSE_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRATION);
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 토큰에서 권한 가져오기
    public UserRole getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return UserRole.valueOf(claims.get(AUTHORIZATION_KEY).toString());
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return substringToken(bearerToken);
        }
        throw new CustomException(ErrorCode.HEADER_NOT_FOUND);
    }


    // JWT 토큰의 Bearer 접두사 제거 후 반환
    public String substringToken(String token) {
        return token.substring(BEARER_PREFIX.length()); // "Bearer " 접두사 제거
    }

    // 블랙리스트
    public String getLoginIdFromToken(String token) {
        return getUserInfoFromToken(token).getSubject();
    }
    // 블랙리스트에 토큰 추가
    public void createblacklistToken(String token) {
        long expirationMillis = calculateExpirationMillis();
        String loginId = getLoginIdFromToken(token);
        redisUtil.setBlackList(token, loginId, expirationMillis);
    }

    // 블랙리스트에서 토큰 검증
    public boolean isTokenBlacklisted(String token) {
        return redisUtil.hasKeyBlackList(token);
    }

    // 블랙리스트에서 토큰 제거
    public void removeTokenFromBlacklist(String token) {
        redisUtil.deleteBlackList(token);
    }

    // 만료 시간 계산
    private long calculateExpirationMillis() {
        return refreshTokenExpiration * 1000; // Convert seconds to milliseconds
    }

}

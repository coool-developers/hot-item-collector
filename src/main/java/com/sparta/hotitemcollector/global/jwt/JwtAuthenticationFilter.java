package com.sparta.hotitemcollector.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.hotitemcollector.domain.security.UserDetailsServiceImpl;
import com.sparta.hotitemcollector.domain.user.UserRole;
import com.sparta.hotitemcollector.global.common.CommonErrorResponse;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT 인증")
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${security.permitted-urls}")
    private String permittedUrlsString;

    private List<String> permittedUrls;

    @Override
    public void afterPropertiesSet() {
        // String to List conversion
        permittedUrls = Arrays.stream(permittedUrlsString.split(","))
                .map(String::trim) // Remove any extra spaces
                .collect(Collectors.toList());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // GET 요청의 /users/profile/** 경로 필터링 제외
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String headerValue = request.getHeader("Authorization");
            if (headerValue == null) {
                log.info("Permitted URL, skipping JWT authentication.");
                filterChain.doFilter(request, response);
                return;
            }
        }


        // URL이 permitAll 리스트에 있는 경우 필터를 건너뜁니다.
        if (permittedUrls.stream().anyMatch(url -> url.equals(request.getRequestURI()))) {
            log.info("Permitted URL, skipping JWT authentication.");
            filterChain.doFilter(request, response);
            return;
        }
        log.info(request.getRequestURI());

        try {
            log.info("토큰 확인");
            String tokenValue = jwtUtil.getJwtFromHeader(request, AUTHORIZATION_HEADER);

                // 블랙리스트 토큰인지 검사
                if (jwtUtil.isTokenBlacklisted(tokenValue)) {
                    log.info("블랙 리스트 토큰");
                    throw new CustomException(ErrorCode.INVALID_TOKEN);
                }

            jwtUtil.validateToken(tokenValue);

            String userId = jwtUtil.getLoginIdFromToken(tokenValue);
            UserRole role = jwtUtil.getRoleFromToken(tokenValue);

            setAuthentication(userId, role);

        } catch (CustomException e) {
            handleException(response, e.getMessage(), e.getStatusCode());
            return;
        } catch (Exception e) {
            log.error("Authentication Error: {}", e.getMessage());
            handleException(response, "Authentication Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }
        filterChain.doFilter(request,response);

    }

    public void setAuthentication(String userId, UserRole role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId, role);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String userId, UserRole role) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        Collection<? extends GrantedAuthority> authorities = getAuthorities(role);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserRole role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }


    private void handleException(HttpServletResponse res, String message, HttpStatus httpStatus) throws IOException {
        res.setStatus(httpStatus.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        CommonErrorResponse errorResponse = CommonErrorResponse.builder()
                .message(message)
                .error(httpStatus.getReasonPhrase())
                .statusCode(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        mapper.registerModule(javaTimeModule);

        res.getWriter().write(mapper.writeValueAsString(errorResponse));
    }

}
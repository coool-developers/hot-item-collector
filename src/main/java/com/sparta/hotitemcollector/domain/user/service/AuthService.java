package com.sparta.hotitemcollector.domain.user.service;

import com.sparta.hotitemcollector.domain.cart.CartService;
import com.sparta.hotitemcollector.domain.token.Token;
import com.sparta.hotitemcollector.domain.token.TokenService;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.domain.user.UserRole;
import com.sparta.hotitemcollector.domain.user.UserStatus;
import com.sparta.hotitemcollector.domain.user.dto.LoginReqeustDto;
import com.sparta.hotitemcollector.domain.user.dto.LoginResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.RefreshRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.SignupRequestDto;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import com.sparta.hotitemcollector.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserService 로그")
public class AuthService {

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    public void signup(SignupRequestDto signupRequestDto) {
        Optional<User> finduser = userRepository.findByLoginId(signupRequestDto.getLoginId());

        if (finduser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        String password = signupRequestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword)
                .username(signupRequestDto.getUsername())
                .nickname(signupRequestDto.getNickname())
                .build();

        User saveUser = userRepository.save(user);
    }


    public LoginResponseDto login(LoginReqeustDto loginReqeustDto) {
        User finduser = userRepository.findByLoginId(loginReqeustDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        checkUserStatus(finduser);

        if (!passwordEncoder.matches(loginReqeustDto.getPassword(), finduser.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        String access = jwtUtil.createAccessToken(finduser.getLoginId(), finduser.getRole());
        String refresh = jwtUtil.createRefreshToken(finduser.getLoginId(), finduser.getRole());


        // 토큰을 데이터베이스에 저장
        Optional<Token> optionalToken = tokenService.findRefreshToken(finduser);
        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            tokenService.updateToken(token, refresh);
        } else {
            tokenService.saveToken(finduser, refresh);
        }

        return new LoginResponseDto("Bearer " + access, refresh);
    }

    public LoginResponseDto refreshToken(RefreshRequestDto refreshRequestDto) {
        String refreshToken = refreshRequestDto.getRefresh();

        // 토큰 유효성 및 만료 확인
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String loginId = claims.getSubject();
        UserRole role = jwtUtil.getRoleFromToken(refreshToken);


        User finduser = findByLoginId(loginId);

        // DB에 저장된 리프레시 토큰 검증
        Optional<Token> optionalToken = tokenService.findRefreshToken(finduser);
        if (!optionalToken.get().getRefreshToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.UNMATCHED_TOKEN);
        }

        String access = jwtUtil.createAccessToken(loginId, role);
        String refresh = jwtUtil.createRefreshToken(loginId, role);

        tokenService.updateToken(optionalToken.get(),refresh);

        return new LoginResponseDto("Bearer " + access, refresh);
    }


    public void logout(String accessToken, User user) {
        // RefreshToken 삭제
        deleteRefreshToken(user);
        // AccessToken 블랙리스트 추가
        addBlackListToken(accessToken);
    }


    public void withdraw(String accessToken, User user) {
        // RefreshToken 삭제
        deleteRefreshToken(user);

        // AccessToken 블랙리스트 추가
        addBlackListToken(accessToken);

        // 유저 상태 변경
        User finduser = findByLoginId(user.getLoginId());
        finduser.updateStatus(UserStatus.WITHDRAW);
        userRepository.save(finduser);
    }

    /**
     * accessToken 블랙리스트 추가
     *
     * @param accessToken AccessToken
     */
    public void addBlackListToken(String accessToken) {
        // accessToken 블랙리스트 추가
        String tokenValue = jwtUtil.substringToken(accessToken);

        // 토큰 유효한지 확인 - 로그아웃 유무 토큰이 유효한지
        jwtUtil.validateToken(tokenValue);

        // 블랙리스트에 토큰 추가
        jwtUtil.createblacklistToken(tokenValue);
        if (!jwtUtil.isTokenBlacklisted(tokenValue)) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DB에 저장된 RefreshToken 삭제
     *
     * @param user 유저
     */
    public void deleteRefreshToken(User user) {
        User finduser = findByLoginId(user.getLoginId());

        // 회원 상태 확인
        checkUserStatus(finduser);

        // refreshToken 삭제
        Optional<Token> optionalToken = tokenService.findRefreshToken(user);
        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            tokenService.deleteToken(token);
        }
    }


    /**
     * username 을 통해 유저 정보 가져오기
     *
     * @param loginId 사용자 ID
     * @return 유저 정보
     */
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );
    }

    /**
     * 회원 가입상태 검증 (정상, 탈퇴)
     *
     * @param user 요청한 유저 정보
     */
    public void checkUserStatus(User user) {
        if (!user.isExist()) {
            throw new CustomException(ErrorCode.WITHDRAW_USER);
        }
    }


}

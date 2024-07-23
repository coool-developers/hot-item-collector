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

        userRepository.save(user);
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
        Optional<Token> optionalToken = tokenService.checkToken(finduser);
        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            tokenService.updateToken(token, refresh);
        } else {
            tokenService.saveToken(finduser, refresh);
        }

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
        User finduser = findByUserId(user.getLoginId());
        finduser.updateStatus(UserStatus.WITHDRAW);
        userRepository.save(finduser);
    }

    /**
     * accessToken 블랙리스트 추가
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
     * @param user 유저
     */
    public void deleteRefreshToken(User user) {
        User finduser = findByUserId(user.getLoginId());

        // 회원 상태 확인
        checkUserStatus(finduser);

        // refreshToken 삭제
        Optional<Token> optionalToken = tokenService.checkToken(user);
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
    public User findByUserId(String loginId) {
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

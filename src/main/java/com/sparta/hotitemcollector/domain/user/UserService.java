package com.sparta.hotitemcollector.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.hotitemcollector.domain.follow.FollowRepository;
import com.sparta.hotitemcollector.domain.s3.service.S3Service;
import com.sparta.hotitemcollector.domain.token.Token;
import com.sparta.hotitemcollector.domain.token.TokenService;
import com.sparta.hotitemcollector.domain.user.dto.auth.LoginReqeustDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.LoginResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.RefreshRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.auth.SignupRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ConfirmPasswordDto;
import com.sparta.hotitemcollector.domain.user.dto.user.GetMyProfileDto;
import com.sparta.hotitemcollector.domain.user.dto.user.GetUserProfileDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileImageRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileImageResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileResponseDto;
import com.sparta.hotitemcollector.domain.user.dto.user.UserAddressDto;
import com.sparta.hotitemcollector.domain.user.dto.user.updatePasswordRequestDto;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import com.sparta.hotitemcollector.global.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserService 로그")
public class UserService {
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private final FollowRepository followRepository;

    public void signup(SignupRequestDto signupRequestDto) {
        Optional<User> finduser = userRepository.findByLoginId(signupRequestDto.getLoginId());

        if (finduser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }
        if(userRepository.existsByNicknameContainingIgnoreCase(signupRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        String password = signupRequestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        String defaultFilename = "기본 프로필 이미지";
        String defaultImageUrl = "https://www.pngarts.com/files/10/Default-Profile-Picture-PNG-Download-Image.png";
        ProfileImageRequestDto requestDto = new ProfileImageRequestDto(defaultFilename,defaultImageUrl);

        User user = User.builder()
                .loginId(signupRequestDto.getLoginId())
                .password(encodedPassword)
                .username(signupRequestDto.getUsername())
                .nickname(signupRequestDto.getNickname())
                .build();

        ProfileImage profileImage = new ProfileImage(requestDto,user);
        user.updateProfileImage(profileImage);
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
        if(optionalToken.isPresent()){
            if (!optionalToken.get().getRefreshToken().equals(refreshToken)) {
                throw new CustomException(ErrorCode.UNMATCHED_TOKEN);
            }
        }else{
            throw new CustomException(ErrorCode.INVALID_TOKEN);
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


    @Transactional
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




    public ProfileResponseDto updateProfile(ProfileRequestDto requestDto, User user) {
        User findUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (requestDto.getNickname() != null) {
            findUser.setNickname(requestDto.getNickname());
        }
        if (requestDto.getPhoneNumber() != null) {
            findUser.setPhoneNumber(requestDto.getPhoneNumber());
        }
        if (requestDto.getAddress() != null) {
            findUser.setAddress(requestDto.getAddress());
        }
        if (requestDto.getInfo() != null) {
            findUser.setInfo(requestDto.getInfo());
        }

        // 요청으로 사진을 보냄
        if (requestDto.getProfileImage() != null) {
            // 해당 유저 프로필 사진 유무 확인
            ProfileImage existingProfileImage = findUser.getProfileImage();
            // 프로필 사진 있을 경우
            if (existingProfileImage != null) {
                // S3에서 기존 이미지 삭제
                s3Service.deleteImage(existingProfileImage.getFilename());

                // 테이블에 담겨있던 기존 이미지 새로운 이미지로 변경
                existingProfileImage.updateImage(requestDto.getProfileImage());
                profileImageRepository.save(existingProfileImage);
                findUser.updateProfileImage(existingProfileImage);
            }else{ // 프로필 사진 없을 경우
                ProfileImageRequestDto profileImageRequestDto = new ProfileImageRequestDto(requestDto.getProfileImage().getFilename(),requestDto.getProfileImage().getImageUrl());
                ProfileImage newProfileImage = new ProfileImage(profileImageRequestDto,findUser);
                profileImageRepository.save(newProfileImage);
                findUser.updateProfileImage(newProfileImage);
            }
            User saveUser = userRepository.save(findUser);

            ProfileImageResponseDto profileImageResponseDto = new ProfileImageResponseDto(saveUser.getProfileImage());
            return new ProfileResponseDto(saveUser, profileImageResponseDto);
        }else{
            User saveUser = userRepository.save(findUser);
            return new ProfileResponseDto(saveUser, null);
        }

    }


    public void updatePassword(updatePasswordRequestDto requestDto, User user) {
        // 유저를 찾고, 없으면 예외를 발생시킵니다.
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 기존 비밀번호가 맞는지 확인합니다.
        if (!passwordEncoder.matches(requestDto.getOldPassword(), findUser.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 새로운 비밀번호 설정
        findUser.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        // 변경된 비밀번호를 저장합니다.
        userRepository.save(findUser);
    }

    public GetUserProfileDto getUserProfile(Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Long followerCount = followRepository.countByFollowing(findUser);
        ProfileImageResponseDto profileImageResponseDto = new ProfileImageResponseDto(findUser.getProfileImage());
            return new GetUserProfileDto(findUser,profileImageResponseDto,followerCount);
    }

    public GetMyProfileDto getMyProfile(User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Long followerCount = followRepository.countByFollowing(findUser);
        if(findUser.getProfileImage()!=null){
            ProfileImageResponseDto profileImageResponseDto = new ProfileImageResponseDto(findUser.getProfileImage());
            return new GetMyProfileDto(findUser,profileImageResponseDto,followerCount);
        }
        return new GetMyProfileDto(findUser,null,followerCount);

    }


    public UserAddressDto getUserAddress(User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new UserAddressDto(findUser);
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



    /**
     * 닉네임으로 유저 리스트 검색
     * @param nickname
     * @return
     */
    public List<User> findByNicknameContainingIgnoreCase (String nickname) {
        return userRepository.findByNicknameContainingIgnoreCase(nickname);
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public void confirmPassword(ConfirmPasswordDto requestDto, User user) {
        User finduser = userRepository.findByLoginId(user.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), finduser.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}

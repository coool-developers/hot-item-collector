package com.sparta.hotitemcollector.domain.user.service;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.domain.user.dto.user.*;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponseDto updateProfile(ProfileRequestDto requestDto, User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_USER));

        // 유저 정보 업데이트 (요청된 필드만 업데이트)
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
        if(requestDto.getProfileImage() != null){
            findUser.setProfileImage(requestDto.getProfileImage());
        }
        // 유저 저장
        User saveUser = userRepository.save(findUser);
        return new ProfileResponseDto(saveUser);
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

    public GetUserProfileDto getUserProfile(Long userId, Optional<UserDetailsImpl> currentUser) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if(!currentUser.isEmpty()){
            boolean isOwnProfile = findUser.getId().equals(currentUser.get().getUser().getId());

            return new GetUserProfileDto(findUser, isOwnProfile);
        }else{
            return new GetUserProfileDto(findUser, false);
        }

    }


    public UserAddressDto getUserAddress(User user) {
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return new UserAddressDto(findUser);
    }



    /**
     * 닉네임으로 유저 리스트 검색
     * @param nickname
     * @return
     */
    public List<User> findByNicknameContainingIgnoreCase (String nickname) {
        return userRepository.findByNicknameContainingIgnoreCase(nickname);
    }


}

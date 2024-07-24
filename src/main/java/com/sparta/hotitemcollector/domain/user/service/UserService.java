package com.sparta.hotitemcollector.domain.user.service;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.domain.user.UserRepository;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileRequestDto;
import com.sparta.hotitemcollector.domain.user.dto.user.ProfileResponseDto;
import com.sparta.hotitemcollector.global.exception.CustomException;
import com.sparta.hotitemcollector.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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



    /**
     * 닉네임으로 유저 리스트 검색
     * @param nickname
     * @return
     */
    public List<User> findByNicknameContainingIgnoreCase (String nickname) {
        return userRepository.findByNicknameContainingIgnoreCase(nickname);
    }
}

package com.sparta.hotitemcollector.domain.chat.chatroom;

import com.sparta.hotitemcollector.domain.chat.chatroom.dto.ChatRoomCreationDto;
import com.sparta.hotitemcollector.domain.chat.chatroom.dto.ChatRoomDetailDto;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import com.sparta.hotitemcollector.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chatroom")
    public ResponseEntity<CommonResponse> createChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ChatRoomCreationDto roomCreationDto) {
        ChatRoomDetailDto chatRoom = chatRoomService.createChatRoom(userDetails.getUser().getNickname(),roomCreationDto.getSeller());
        CommonResponse response = new CommonResponse<>("채팅방 생성 및 조회 완료", 200, chatRoom);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("chatrooms/list")
    private ResponseEntity<CommonResponse> getAllChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatRoomDetailDto> chatRoomDetailDtoList = chatRoomService.getAllChatRoomByUser(userDetails.getUser().getNickname());
        CommonResponse response = new CommonResponse<>("채팅방 목록 조회", 200, chatRoomDetailDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/chatroom/{roomId}")
    public ResponseEntity<CommonResponse> getChatRoom(@PathVariable String roomId) {
        ChatRoomDetailDto chatRoom = chatRoomService.getChatRoom(roomId);
        CommonResponse response = new CommonResponse<>("채팅방 진입 완료", 200, chatRoom);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

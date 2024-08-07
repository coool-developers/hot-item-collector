package com.sparta.hotitemcollector.domain.chat.chatroom;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chatroom")
    public ChatRoom createChatRoom(@RequestBody ChatRoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatRoomService.findOrCreateChatRoom(requestDto.getSeller(), userDetails.getUser().getNickname());
    }

    @GetMapping("/chatroom/{roomId}")
    public ChatRoom getChatRoom(@PathVariable String roomId) {
        return chatRoomService.getChatRoomById(roomId);
    }
}

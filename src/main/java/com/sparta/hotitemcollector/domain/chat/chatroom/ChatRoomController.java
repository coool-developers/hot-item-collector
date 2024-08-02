package com.sparta.hotitemcollector.domain.chat.chatroom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chatroom")
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.createChatRoom(chatRoom);
    }

    @GetMapping("/chatroom/{roomId}")
    public ChatRoom getChatRoom(@PathVariable String roomId) {
        return chatRoomService.getChatRoomById(roomId);
    }
}

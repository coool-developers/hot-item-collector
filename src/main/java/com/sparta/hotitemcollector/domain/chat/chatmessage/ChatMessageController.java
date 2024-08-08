package com.sparta.hotitemcollector.domain.chat.chatmessage;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/chatmessage")
    public ChatMessageDto sendMessage(@RequestBody ChatMessageRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatMessageService.sendMessage(requestDto.getRoomId(), requestDto.getMessage(), userDetails.getUser().getNickname());
    }

    @GetMapping("/chatmessage/room/{roomId}")
    public List<ChatMessageDto> getMessagesByRoomId(@PathVariable String roomId) {
        return chatMessageService.getMessagesByRoomId(roomId);
    }
}

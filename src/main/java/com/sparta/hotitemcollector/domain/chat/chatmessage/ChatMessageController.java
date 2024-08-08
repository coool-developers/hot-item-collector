package com.sparta.hotitemcollector.domain.chat.chatmessage;

import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessageDto sendMessage(@RequestBody ChatMessageRequestDto requestDto) {
        // 현재 인증된 사용자 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sender = (authentication != null && authentication.getPrincipal() instanceof UserDetails)
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : "unknown";
        return chatMessageService.sendMessage(requestDto.getRoomId(), requestDto.getMessage(), sender);
    }

    @MessageMapping("/join")
    @SendTo("/topic/rooms")
    public List<ChatMessageDto> getMessagesByRoomId(@PathVariable String roomId) {
        return chatMessageService.getMessagesByRoomId(roomId);
    }
}

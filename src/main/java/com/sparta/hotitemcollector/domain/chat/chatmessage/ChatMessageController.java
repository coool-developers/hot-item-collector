package com.sparta.hotitemcollector.domain.chat.chatmessage;

import com.sparta.hotitemcollector.domain.chat.RedisPublisher;
import com.sparta.hotitemcollector.domain.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMongoRepositories
@EnableMongoAuditing
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final RedisPublisher redisPublisher;

    @PostMapping("/chat/message")
    public ChatMessage createChatMessage(@RequestBody ChatMessage chatMessage, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatMessage sendMessage = ChatMessage.builder()
                .sender(userDetails.getUser().getNickname())
                .message(chatMessage.getMessage())
                .roomId(chatMessage.getRoomId())
                .build();
        ChatMessage savedMessage = chatMessageService.createChatMessage(sendMessage);
        redisPublisher.publishMessage("hotitem", savedMessage);
        return savedMessage;
    }

    @GetMapping("/chat/messages/{roomId}")
    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
        return chatMessageService.getAllChatMessages(roomId);
    }
}

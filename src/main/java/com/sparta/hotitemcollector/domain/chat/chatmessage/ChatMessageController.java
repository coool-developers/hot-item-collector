package com.sparta.hotitemcollector.domain.chat.chatmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMongoRepositories
@EnableMongoAuditing
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("/chat/message")
    public ChatMessage createChatMessage(@RequestBody ChatMessage chatMessage) {
        return chatMessageService.createChatMessage(chatMessage);
    }

    @GetMapping("/chat/messages/{roomId}")
    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
        return chatMessageService.getAllChatMessages(roomId);
    }
}

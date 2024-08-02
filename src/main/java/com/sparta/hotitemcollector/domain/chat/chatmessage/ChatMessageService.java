package com.sparta.hotitemcollector.domain.chat.chatmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage createChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getAllChatMessages(String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }
}

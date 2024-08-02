package com.sparta.hotitemcollector.domain.chat.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }
}

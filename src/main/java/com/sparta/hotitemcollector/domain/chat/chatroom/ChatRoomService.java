package com.sparta.hotitemcollector.domain.chat.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findOrCreateChatRoom(String seller, String buyer) {
        ChatRoom chatRoom = chatRoomRepository.findBySellerAndBuyer(seller, buyer);

        // 다른 방식으로 유효성 검사할 수 있도록
        if (chatRoom == null) {
            // 채팅방이 존재하지 않으면 새로 생성
            String roomId = generateRoomId(seller, buyer);
            String roomName = "Chat with " + seller;
            chatRoom = new ChatRoom(roomId, roomName, seller, buyer);
            chatRoomRepository.save(chatRoom);
        }

        return chatRoom;
    }

    private String generateRoomId(String seller, String buyer) {
        // 채팅방 ID 생성 로직 (예: seller와 buyer의 ID를 조합)
        return seller + "_" + buyer;
    }

    public ChatRoom getChatRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }
}

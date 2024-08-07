package com.sparta.hotitemcollector.domain.chat.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(String roomId);

    ChatRoom findBySellerAndBuyer(String seller, String buyer);
}

package com.sparta.hotitemcollector.domain.chat.chatroom;

import lombok.Getter;

@Getter
public class ChatRoomDto {
    private String roomId;
    private String roomName;
    private String seller;
    private String buyer;

    public ChatRoomDto(ChatRoom chatRoom){
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.seller = chatRoom.getSeller();
        this.buyer = chatRoom.getBuyer();
    }
}

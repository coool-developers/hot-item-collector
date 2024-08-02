package com.sparta.hotitemcollector.domain.chat.chatroom;

import lombok.Getter;

@Getter
public class ChatRoomDto {
    private Long id;
    private String roomId;
    private String roomName;

    public ChatRoomDto(ChatRoom chatRoom){
        this.id = chatRoom.getId();
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
    }
}

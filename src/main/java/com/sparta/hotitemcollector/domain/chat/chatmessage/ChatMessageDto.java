package com.sparta.hotitemcollector.domain.chat.chatmessage;

import lombok.Getter;

@Getter
public class ChatMessageDto {
    private String id;
    private String roomId;
    private String sender;
    private String message;
    public ChatMessageDto(String id, String roomId, String sender, String message) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}

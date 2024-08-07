package com.sparta.hotitemcollector.domain.chat.chatmessage;

import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_message")
@Getter
public class ChatMessage extends Timestamped {
    @Id
    private String id;
    private String roomId;
    private String message;
    private String sender;

    @Builder
    public ChatMessage(String id, String roomId, String message, String sender) {
        this.id = id;
        this.roomId = roomId;
        this.message = message;
        this.sender = sender;
    }
}

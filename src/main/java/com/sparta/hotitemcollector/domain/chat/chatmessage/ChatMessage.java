package com.sparta.hotitemcollector.domain.chat.chatmessage;

import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "chat_message")
public class ChatMessage extends Timestamped {
    @Id
    private String id;
    private String roomId;
    private String message;
    private String sender;

}

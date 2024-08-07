package com.sparta.hotitemcollector.domain.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hotitemcollector.domain.chat.chatmessage.ChatMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RedisSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ChatMessage chatMessage = objectMapper.readValue(body, ChatMessage.class);
            System.out.println("Message received: " + chatMessage.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

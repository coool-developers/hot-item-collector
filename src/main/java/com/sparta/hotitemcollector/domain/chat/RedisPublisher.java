package com.sparta.hotitemcollector.domain.chat;

import com.sparta.hotitemcollector.domain.chat.chatmessage.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publishMessage(String topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic, message);
    }
}

package com.sparta.hotitemcollector.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, String> redisTemplate;

    public void publishMessage(String topic, String message) {
        redisTemplate.convertAndSend(topic, message);
    }
}

package com.sparta.hotitemcollector.domain.chat;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber implements MessageListener {

    /**
     * Redis Message를 수신하는 리스너 설정
     * @param message
     */

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Message received" + message.toString());
    }
}

package com.sparta.hotitemcollector.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final RedisPublisher redisPublisher;

    @PostMapping("/chat/publish")
    public void publishMessage(@RequestParam String message){
        redisPublisher.publishMessage("hotitem", message);
    }
}

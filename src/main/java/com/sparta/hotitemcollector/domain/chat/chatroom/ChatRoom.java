package com.sparta.hotitemcollector.domain.chat.chatroom;

import com.sparta.hotitemcollector.domain.user.User;
import com.sparta.hotitemcollector.global.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;
    private String roomName;
    private String sendUser;
    private String receiveUser;

    public ChatRoom(String roomId, String roomName, String sendUser, String receiveUser) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;

    }
}

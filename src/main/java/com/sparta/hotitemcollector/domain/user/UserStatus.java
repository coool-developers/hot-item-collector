package com.sparta.hotitemcollector.domain.user;

public enum UserStatus {
    NORMAL("NORMAL"),
    WITHDRAW("WITHDRAW");


    private String status;
    UserStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}

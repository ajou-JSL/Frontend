package com.example.moum.data.entity;

import java.time.LocalDateTime;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private Integer chatroomId;
    private LocalDateTime timestamp;
    private boolean isSentByMe;

    public Chat(String sender, String receiver, String message, Integer chatroomId, LocalDateTime timestamp){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.chatroomId = chatroomId;
        this.timestamp = timestamp;
        this.isSentByMe = sender.equals(receiver);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setChatroomId(Integer chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSentByMe(boolean sentByMe) {
        isSentByMe = sentByMe;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Integer getChatroomId() {
        return chatroomId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }
}

package com.example.moum.data.dto;

import java.time.LocalDateTime;

public class ChatStreamResponse {
    private String sender;
    private String receiver;
    private String message;
    private int chatroomId;
    private LocalDateTime timestamp;
    private String timestampFormatted;

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setTimestampFormatted(String timestampFormatted) {
        this.timestampFormatted = timestampFormatted;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTimestampFormatted() {
        return timestampFormatted;
    }
}

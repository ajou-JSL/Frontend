package com.example.moum.data.entity;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chat {
    private String sender;
    private String message;
    private Integer chatroomId;
    private LocalDateTime timestamp;
    @Nullable
    private String timestampFormatted;
    private boolean isSentByMe;
    private String profileUrl;

    public Chat(String sender, String message, Integer chatroomId, LocalDateTime timestamp){
        this.sender = sender;
        this.message = message;
        this.chatroomId = chatroomId;
        this.timestamp = timestamp;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Chat(String sender, String message, Integer chatroomId, String timestamp){
        this.sender = sender;
        this.message = message;
        this.chatroomId = chatroomId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        this.timestamp = LocalDateTime.parse(timestamp, formatter);
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setChatroomId(Integer chatroomId) {
        this.chatroomId = chatroomId;
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

    public void setTimestampFormatted(String timestampFormatted) {
        this.timestampFormatted = timestampFormatted;
    }

    public String getProfileUrl() {
        return profileUrl;
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


    public String getSender() {
        return sender;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }

    public String getTimestampFormatted() {
        return timestampFormatted;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", chatroomId=" + chatroomId +
                ", timestamp=" + timestamp +
                '}';
    }
}

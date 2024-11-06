package com.example.moum.data.dto;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatStreamResponse {
    private String sender;
    private String receiver;
    private String message;
    private int chatroomId;
    private String timestamp;
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

    public void setTimestamp(String timestamp) {
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

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public LocalDateTime getTimestamp() {
//        if (timestamp != null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss.SSS");
//            return LocalDateTime.parse(timestamp, formatter);
//        }
//        return null;
//    }


    public String getTimestamp() {
        return timestamp;
    }

    public String getTimestampFormatted() {
        return timestampFormatted;
    }
}

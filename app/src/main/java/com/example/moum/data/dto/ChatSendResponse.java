package com.example.moum.data.dto;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.List;

public class ChatSendResponse {
    private int status;
    private String code;
    private String message;
    private SentChat data;

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public SentChat getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(SentChat data) {
        this.data = data;
    }

    static public class SentChat {
        private String sender;
        private String receiver;
        private String message;
        private Integer chatroomId;
        private List<Integer> timestamp;
        private String timestampFormatted;

        public SentChat(String sender, String receiver, String message, Integer chatroomId, List<Integer> timestamp, String timestampFormatted){
            this.sender = sender;
            this.receiver = receiver;
            this.message = message;
            this.chatroomId = chatroomId;
            this.timestamp = timestamp;
            this.timestampFormatted = timestampFormatted;
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

        public void setTimestamp(List<Integer> timestamp) {
            this.timestamp = timestamp;
        }

        public void setTimestampFormatted(String timestampFormatted) {
            this.timestampFormatted = timestampFormatted;
        }

        public String getMessage() {
            return message;
        }

        public Integer getChatroomId() {
            return chatroomId;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public LocalDateTime getTimestamp() {
            if (timestamp != null && timestamp.size() >= 6) {
                return LocalDateTime.of(timestamp.get(0), timestamp.get(1), timestamp.get(2),
                        timestamp.get(3), timestamp.get(4), timestamp.get(5));
            }
            return null;
        }

        public String getReceiver() {
            return receiver;
        }

        public String getSender() {
            return sender;
        }

        public String getTimestampFormatted() {
            return timestampFormatted;
        }
    }
}

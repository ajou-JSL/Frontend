package com.example.moum.data.dto;

import java.time.LocalDateTime;

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
        private LocalDateTime timestamp;

        public SentChat(String sender, String receiver, String message, Integer chatroomId, LocalDateTime timestamp){
            this.sender = sender;
            this.receiver = receiver;
            this.message = message;
            this.chatroomId = chatroomId;
            this.timestamp = timestamp;
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
    }
}

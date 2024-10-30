package com.example.moum.data.dto;

public class ChatSendRequest {
    private String sender;
    private String receiver;
    private String message;

    public ChatSendRequest(String sender, String receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }
    public String getMessage() {
        return message;
    }
    public String getSender() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

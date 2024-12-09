package com.example.moum.data.dto;

import com.google.gson.annotations.SerializedName;

public class ChatSendRequest {
    @SerializedName("message")
    private String message;

    public ChatSendRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

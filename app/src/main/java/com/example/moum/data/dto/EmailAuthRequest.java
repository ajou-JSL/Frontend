package com.example.moum.data.dto;

import com.google.gson.annotations.SerializedName;

public class EmailAuthRequest {
    @SerializedName("email")
    private String email;

    public EmailAuthRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

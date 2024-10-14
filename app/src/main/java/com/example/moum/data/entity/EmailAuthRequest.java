package com.example.moum.data.entity;

public class EmailAuthRequest {
    private String email;

    public EmailAuthRequest(String email){
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.example.moum.data.entity;

public class EmailCodeRequest {
    private String emailCode;

    public EmailCodeRequest(String emailCode){
        this.emailCode = emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getEmailCode() {
        return emailCode;
    }
}

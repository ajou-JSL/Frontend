package com.example.moum.data.entity;

public class EmailCodeRequest {
    private String email;
    private String verifyCode;

    public EmailCodeRequest(String email, String verifyCode){

        this.email = email;
        this.verifyCode = verifyCode;
    }

    public String getEmail() {
        return email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}

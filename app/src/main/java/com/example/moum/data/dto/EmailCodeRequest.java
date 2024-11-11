package com.example.moum.data.dto;

import com.google.gson.annotations.SerializedName;

public class EmailCodeRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("verifyCode")
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

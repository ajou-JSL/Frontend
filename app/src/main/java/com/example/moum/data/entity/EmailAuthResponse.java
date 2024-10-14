package com.example.moum.data.entity;

public class EmailAuthResponse {

    private String success;
    private String verifyCode;

    public String getSuccess() {
        return success;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

}

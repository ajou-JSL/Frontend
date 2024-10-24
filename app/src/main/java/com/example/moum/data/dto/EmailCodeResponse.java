package com.example.moum.data.dto;

public class EmailCodeResponse {
    private int status;
    private String code;
    private String message;
    private String data;

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
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

    public void setData(String data) {
        this.data = data;
    }
}

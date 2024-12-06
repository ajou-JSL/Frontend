package com.example.moum.data.dto;

public class SuccessResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
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

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String str = "SuccessResponse{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'';
        if (data != null) {
            str = str + ", data=" + data.toString() + '}';
        }
        return str;
    }

}
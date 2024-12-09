package com.example.moum.data.dto;

public class RefreshRequest {
    private String refresh;

    public RefreshRequest(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
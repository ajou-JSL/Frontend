package com.example.moum.data.entity;

public class Token {

    private String access;
    private String refresh;

    public Token(String access, String refresh){
        this.access = access;
        this.refresh = refresh;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }
}

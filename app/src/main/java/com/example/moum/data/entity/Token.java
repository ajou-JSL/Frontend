package com.example.moum.data.entity;

public class Token {

    private String access;
    private String refresh;
    private String memberId;

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

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getMemberId() {
        return memberId;
    }
}

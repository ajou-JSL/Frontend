package com.example.moum.data.entity;

public class Token {
    private String access;
    private String refresh;
    private String uesrname;
    private Integer id;

    public Token(String access, String refresh, Integer id){
        this.access = access;
        this.refresh = refresh;
        this.id = id;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public void setUesrname(String uesrname) {
        this.uesrname = uesrname;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getUesrname() {
        return uesrname;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Token{" +
                "access='" + access + '\'' +
                ", refresh='" + refresh + '\'' +
                '}';
    }
}

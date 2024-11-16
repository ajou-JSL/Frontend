package com.example.moum.data.entity;

public class Token {
    private String access;
    private String refresh;
    private String uesrname;
    private Integer id;
    private Member member;

    public Token(String access, String refresh, Integer id, Member member){
        this.access = access;
        this.refresh = refresh;
        this.id = id;
        this.member = member;
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

    public void setMember(Member member) {
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "Token{" +
                "access='" + access + '\'' +
                ", refresh='" + refresh + '\'' +
                '}';
    }
}

package com.example.moum.data.dto;

public class MemberProfileRankResponse {
    private Integer memberId;
    private String memberName;
    private String memberUsername;
    private Integer exp;
    private String tier;
    private String fileUrl;

    public String getMemberUsername() {
        return memberUsername;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public String getTier() {
        return tier;
    }

    public Integer getExp() {
        return exp;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
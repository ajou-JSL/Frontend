package com.example.moum.data.entity;

public class ReportMember {
    private Integer id;
    private Integer memberId;
    private String memberUsername;
    private Integer reportedId;
    private String reportedUsername;
    private String type;
    private String details;
    private String reply;
    private Boolean resolved;

    public Integer getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public Integer getReportedId() {
        return reportedId;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public String getReply() {
        return reply;
    }

    public String getReportedUsername() {
        return reportedUsername;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public void setReportedId(Integer reportedId) {
        this.reportedId = reportedId;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setReportedUsername(String reportedUsername) {
        this.reportedUsername = reportedUsername;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public String toString() {
        return "ReportMember{" +
                "reportedId=" + reportedId +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", memberId=" + memberId +
                ", memberUsername='" + memberUsername + '\'' +
                ", reportedUsername='" + reportedUsername + '\'' +
                ", reply='" + reply + '\'' +
                ", resolved=" + resolved +
                '}';
    }
}

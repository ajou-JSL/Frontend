package com.example.moum.data.entity;

public class ReportTeam {
    private Integer id;
    private Integer teamId;
    private String teamName;
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

    public String getTeamName() {
        return teamName;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public Integer getReportedId() {
        return reportedId;
    }

    public Integer getTeamId() {
        return teamId;
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

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
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
        return "ReportTeam{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", reportedId=" + reportedId +
                ", reportedUsername='" + reportedUsername + '\'' +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", reply='" + reply + '\'' +
                ", resolved=" + resolved +
                '}';
    }
}

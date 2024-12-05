package com.example.moum.data.entity;

public class ReportArticle {
    private Integer id;
    private Integer articleId;
    private String articleTitle;
    private Integer reporterId;
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

    public Integer getArticleId() {
        return articleId;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public String getArticleTitle() {
        return articleTitle;
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

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
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
        return "ReportArticle{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", reporterId=" + reporterId +
                ", reportedUsername='" + reportedUsername + '\'' +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", reply='" + reply + '\'' +
                ", resolved=" + resolved +
                '}';
    }
}

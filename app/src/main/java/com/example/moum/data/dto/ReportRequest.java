package com.example.moum.data.dto;

public class ReportRequest {
    private Integer reporterId;
    private String type;
    private String details;

    public ReportRequest(Integer reporterId, String type, String details){
        this.reporterId = reporterId;
        this.type = type;
        this.details = details;
    }
}

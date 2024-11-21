package com.example.moum.data.dto;

public class ReportRequest {
    private Integer reportedId;
    private String type;
    private String details;

    public ReportRequest(Integer reportedId, String type, String details){
        this.reportedId = reportedId;
        this.type = type;
        this.details = details;
    }
}

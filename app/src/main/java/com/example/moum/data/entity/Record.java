package com.example.moum.data.entity;

public class Record {
    private Integer recordId;
    private String recordName;
    private String startDate;
    private String endDate;

    public Record(String recordName, String startDate, String endDate) {
        this.recordName = recordName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public String getRecordName() {
        return recordName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", recordName='" + recordName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}

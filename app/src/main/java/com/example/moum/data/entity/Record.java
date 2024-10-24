package com.example.moum.data.entity;

public class Record {
    private String recordName;
    private String startDate;
    private String endDate;

    public Record(String recordName, String startDate, String endDate){
        this.recordName = recordName;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

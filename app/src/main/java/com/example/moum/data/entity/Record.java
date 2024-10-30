package com.example.moum.data.entity;

import java.time.LocalDate;

public class Record {
    private String recordName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Record(String recordName, LocalDate startDate, LocalDate endDate){
        this.recordName = recordName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getRecordName() {
        return recordName;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

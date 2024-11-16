package com.example.moum.data.entity;

import java.util.ArrayList;

public class Performance {
    private Integer id;
    private String performanceName;
    private String performanceDescription;
    private String performanceLocation;
    private String performanceStartDate;
    private String performanceEndDate;
    private String performancePrice;
    private String performanceImageUrl;
    private ArrayList<Integer> membersId;
    private Integer teamId;
    private Integer moumId;

    public Integer getId() {
        return id;
    }

    public ArrayList<Integer> getMembersId() {
        return membersId;
    }

    public Integer getMoumId() {
        return moumId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getPerformanceDescription() {
        return performanceDescription;
    }

    public String getPerformanceEndDate() {
        return performanceEndDate;
    }

    public String getPerformanceImageUrl() {
        return performanceImageUrl;
    }

    public String getPerformanceLocation() {
        return performanceLocation;
    }

    public String getPerformanceName() {
        return performanceName;
    }

    public String getPerformancePrice() {
        return performancePrice;
    }

    public String getPerformanceStartDate() {
        return performanceStartDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMoumId(Integer moumId) {
        this.moumId = moumId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setMembersId(ArrayList<Integer> membersId) {
        this.membersId = membersId;
    }

    public void setPerformanceDescription(String performanceDescription) {
        this.performanceDescription = performanceDescription;
    }

    public void setPerformanceEndDate(String performanceEndDate) {
        this.performanceEndDate = performanceEndDate;
    }

    public void setPerformanceImageUrl(String performanceImageUrl) {
        this.performanceImageUrl = performanceImageUrl;
    }

    public void setPerformanceLocation(String performanceLocation) {
        this.performanceLocation = performanceLocation;
    }

    public void setPerformanceName(String performanceName) {
        this.performanceName = performanceName;
    }

    public void setPerformancePrice(String performancePrice) {
        this.performancePrice = performancePrice;
    }

    public void setPerformanceStartDate(String performanceStartDate) {
        this.performanceStartDate = performanceStartDate;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "performanceName='" + performanceName + '\'' +
                ", performanceDescription='" + performanceDescription + '\'' +
                ", performanceLocation='" + performanceLocation + '\'' +
                ", performanceStartDate='" + performanceStartDate + '\'' +
                ", performanceEndDate='" + performanceEndDate + '\'' +
                ", performancePrice='" + performancePrice + '\'' +
                ", performanceImageUrl='" + performanceImageUrl + '\'' +
                ", membersId=" + membersId +
                ", teamId=" + teamId +
                ", moumId=" + moumId +
                '}';
    }
}

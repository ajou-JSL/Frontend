package com.example.moum.data.entity;

import java.util.ArrayList;

public class Moum {
    private Integer moumId;
    private String moumName;
    private String moumDescription;
    private String performLocation;
    private String startDate;
    private String endDate;
    private Integer price;
    private String imageUrl;
    private Integer leaderId;
    private String leaderName;
    private Integer teamId;
    private ArrayList<Member> members;
    private ArrayList<Record> records;

    public ArrayList<Record> getRecords() {
        return records;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public Integer getMoumId() {
        return moumId;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getMoumDescription() {
        return moumDescription;
    }

    public String getMoumName() {
        return moumName;
    }

    public String getPerformLocation() {
        return performLocation;
    }

    @Override
    public String toString() {
        return "Moum{" +
                "moumId=" + moumId +
                ", moumName='" + moumName + '\'' +
                ", moumDescription='" + moumDescription + '\'' +
                ", performLocation='" + performLocation + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", leaderId=" + leaderId +
                ", leaderName='" + leaderName + '\'' +
                ", teamId=" + teamId +
                ", members=" + members +
                ", records=" + records +
                '}';
    }
}

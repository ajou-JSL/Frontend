package com.example.moum.data.entity;

import android.net.Uri;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Team {
    private Integer teamId;
    private Integer leaderId;
    private String teamName;
    private String description;
    private String createdAt;
    private String fileUrl;
    private ArrayList<Member> members;
    private ArrayList<Record> records;

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", leaderId=" + leaderId +
                ", teamName='" + teamName + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", members=" + members +
                '}';
    }
}

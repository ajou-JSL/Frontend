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
    private String genre;
    private String location;
    private String createdAt;
    private String fileUrl;
    private ArrayList<Member> members;
    private ArrayList<Record> records;
    private String tier;
    private Integer exp;
    private String videoUrl;

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

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }


    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public void setTier(String tier) {
        this.tier = tier;
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

    public String getGenre() {
        return genre;
    }

    public String getLocation() {
        return location;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public String getTier() {
        return tier;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Integer getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", leaderId=" + leaderId +
                ", teamName='" + teamName + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", location='" + location + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", members=" + members +
                ", records=" + records +
                ", tier='" + tier + '\'' +
                ", exp=" + exp +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}

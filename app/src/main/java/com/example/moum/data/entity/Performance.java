package com.example.moum.data.entity;

import java.util.ArrayList;

public class Performance {
    private Integer id;
    private String performanceName;
    private String performanceDescription;
    private String performanceLocation;
    private String performanceStartDate;
    private String performanceEndDate;
    private Integer performancePrice;
    private String performanceImageUrl;
    private ArrayList<Integer> membersId;
    private Integer teamId;
    private String teamName;
    private Integer moumId;
    private String moumName;
    private Genre genre;
    private ArrayList<Music> musics;
    private Integer likesCount;
    private Integer viewCount;

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

    public String getMoumName() {
        return moumName;
    }

    public ArrayList<Music> getMusics() {
        return musics;
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

    public Integer getPerformancePrice() {
        return performancePrice;
    }

    public String getPerformanceStartDate() {
        return performanceStartDate;
    }

    public String getTeamName() {
        return teamName;
    }

    public Genre getGenre() {
        return genre;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public Integer getViewCount() {
        return viewCount;
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

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setMoumName(String moumName) {
        this.moumName = moumName;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
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

    public void setPerformancePrice(Integer performancePrice) {
        this.performancePrice = performancePrice;
    }

    public void setPerformanceStartDate(String performanceStartDate) {
        this.performanceStartDate = performanceStartDate;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "id=" + id +
                ", performanceName='" + performanceName + '\'' +
                ", performanceDescription='" + performanceDescription + '\'' +
                ", performanceLocation='" + performanceLocation + '\'' +
                ", performanceStartDate='" + performanceStartDate + '\'' +
                ", performanceEndDate='" + performanceEndDate + '\'' +
                ", performancePrice=" + performancePrice +
                ", performanceImageUrl='" + performanceImageUrl + '\'' +
                ", membersId=" + membersId +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", moumId=" + moumId +
                ", moumName='" + moumName + '\'' +
                ", genre=" + genre +
                ", musics=" + musics +
                ", likesCount=" + likesCount +
                ", viewCount=" + viewCount +
                '}';
    }
}

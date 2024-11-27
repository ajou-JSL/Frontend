package com.example.moum.data.dto;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Performance;

import java.util.ArrayList;

public class PerformRequest {
    private String performanceName;
    private String performanceDescription;
    private String performanceLocation;
    private String performanceStartDate;
    private String performanceEndDate;
    private Integer performancePrice;
    private ArrayList<Integer> membersId;
    private Integer teamId;
    private Integer moumId;
    private ArrayList<Music> music;
    private Genre genre;

    public PerformRequest(
            String performanceName,
            String performanceDescription,
            String performanceLocation,
            String performanceStartDate,
            String performanceEndDate,
            Integer performancePrice,
            ArrayList<Integer> membersId,
            Integer teamId,
            Integer moumId,
            ArrayList<Music> music,
            Genre genre
    ) {
        this.performanceName = performanceName;
        this.performanceDescription = performanceDescription;
        this.performanceLocation = performanceLocation;
        this.performanceStartDate = performanceStartDate;
        this.performanceEndDate = performanceEndDate;
        this.performancePrice = performancePrice;
        this.membersId = membersId;
        this.teamId = teamId;
        this.moumId = moumId;
        this.music = music;
        this.genre = genre;
    }
}

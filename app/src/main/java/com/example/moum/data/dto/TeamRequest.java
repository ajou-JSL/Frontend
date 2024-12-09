package com.example.moum.data.dto;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Record;

import java.util.ArrayList;

public class TeamRequest {
    private Integer leaderId;
    private String teamName;
    private String description;
    private Genre genre;
    private String location;
    private ArrayList<Record> records;
    private String videoUrl;

    public TeamRequest(Integer leaderId, String teamName, String description, Genre genre, String location, ArrayList<Record> records,
            String videoUrl) {
        this.leaderId = leaderId;
        this.teamName = teamName;
        this.description = description;
        this.genre = genre;
        this.location = location;
        this.records = records;
        this.videoUrl = videoUrl;
    }
}

package com.example.moum.data.dto;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Record;

import java.util.ArrayList;

public class MoumRequest {
    private String moumName;
    private String moumDescription;
    private String performLocation;
    private String startDate;
    private String endDate;
    private Integer price;
    private Integer leaderId;
    private Integer teamId;
    private ArrayList<Member> members;
    private ArrayList<Record> records;
    private ArrayList<Music> music;
    private Genre genre;

    public MoumRequest(String moumName, String moumDescription, String performLocation, String startDate, String endDate, Integer price,
            Integer leaderId, Integer teamId, ArrayList<Member> members, ArrayList<Record> records, ArrayList<Music> music, Genre genre) {
        this.moumName = moumName;
        this.moumDescription = moumDescription;
        this.performLocation = performLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.leaderId = leaderId;
        this.teamId = teamId;
        this.members = members;
        this.records = records;
        this.music = music;
        this.genre = genre;
    }
}

package com.example.moum.data.dto;

import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Record;

import java.util.ArrayList;

public class MemberProfileUpdateRequest {
    private String name;
    private String username;
    private String profileDescription;
    private String email;
    private String proficiency;
    private String instrument;
    private String address;
    private ArrayList<Record> records;
    private ArrayList<Genre> genres;
    private String videoUrl;

    public MemberProfileUpdateRequest(String name, String username, String profileDescription, String email, String proficiency, String instrument,
            String address, ArrayList<Record> records, ArrayList<Genre> genres, String videoUrl) {
        this.name = name;
        this.username = username;
        this.profileDescription = profileDescription;
        this.email = email;
        this.proficiency = proficiency;
        this.instrument = instrument;
        this.address = address;
        this.records = records;
        this.genres = genres;
        this.videoUrl = videoUrl;
    }

}

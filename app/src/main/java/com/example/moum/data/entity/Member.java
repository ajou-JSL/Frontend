package com.example.moum.data.entity;

import android.net.Uri;

import java.util.ArrayList;

public class Member {
    private Integer id;
    private String name;
    private String username;
    private String profileDescription;
    private String email;
    private ArrayList<Team> teams;
    private Uri profileImageUrl;
    private String proficiency;
    private ArrayList<Record> records;
    private String instrument;
    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfileImageUrl(Uri profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", email='" + email + '\'' +
                ", teams=" + teams +
                ", profileImageUrl=" + profileImageUrl +
                ", proficiency='" + proficiency + '\'' +
                ", records=" + records +
                ", instrument='" + instrument + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

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
    private String profileImageUrl;
    private String proficiency;
    private ArrayList<Record> records;
    private String instrument;
    private String address;
    private String tier;
    private Integer exp;

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfileImageUrl(String profileImageUrl) {
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

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public String getEmail() {
        return email;
    }

    public String getProficiency() {
        return proficiency;
    }

    public String getAddress() {
        return address;
    }

    public String getInstrument() {
        return instrument;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
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

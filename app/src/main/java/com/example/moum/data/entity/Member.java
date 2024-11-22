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
    private ArrayList<Record> memberRecords;
    private String instrument;
    private String address;
    private String tier;
    private Integer exp;
    private ArrayList<Record> moumRecords;
    private String videoUrl;
    private ArrayList<Genre> genres;

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

    public void setMemberRecords(ArrayList<Record> memberRecords) {
        this.memberRecords = memberRecords;
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

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public void setMoumRecords(ArrayList<Record> moumRecords) {
        this.moumRecords = moumRecords;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
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

    public ArrayList<Record> getMemberRecords() {
        return memberRecords;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public ArrayList<Record> getMoumRecords() {
        return moumRecords;
    }

    public Integer getExp() {
        return exp;
    }

    public String getTier() {
        return tier;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
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
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", proficiency='" + proficiency + '\'' +
                ", memberRecords=" + memberRecords +
                ", instrument='" + instrument + '\'' +
                ", address='" + address + '\'' +
                ", tier='" + tier + '\'' +
                ", exp=" + exp +
                ", moumRecords=" + moumRecords +
                ", videoUrl='" + videoUrl + '\'' +
                ", genres=" + genres +
                '}';
    }
}

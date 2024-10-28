package com.example.moum.data.entity;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class User {
    private String name;
    private String password;
    private String passwordCheck;
    private String email;
    private String emailCode;

    private String nickname;
    private File profileImage;
    private String profileDescription;
    private String instrument;
    private String proficiency;
    private String address;
    private ArrayList<Record> records;

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getPasswordCheck() {
        return passwordCheck;
    }
    public String getEmail() {
        return email;
    }
    public String getEmailCode() {
        return emailCode;
    }

    public String getNickname() {
        return nickname;
    }
    public File getProfileImage() {
        return profileImage;
    }
    public String getProfileDescription() {
        return profileDescription;
    }
    public String getInstrument() {
        return instrument;
    }
    public String getProficiency() {
        return proficiency;
    }
    public String getAddress() {
        return address;
    }
    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setProfileImage(File profileImage) {
        this.profileImage = profileImage;
    }
    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }
    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
}

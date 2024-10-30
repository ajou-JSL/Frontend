package com.example.moum.data.dto;

import com.example.moum.data.entity.Record;

import java.util.ArrayList;

public class SignupRequest {
    private String name;
    private String password;
    private String email;

    private String username;
    private String profileDescription;
    private String instrument;
    private String proficiency;
    private String address;
    private ArrayList<Record> records;

    public SignupRequest(String name, String password, String email, String userName, String profileDescription, String instrument, String proficiency, String address, ArrayList<Record> records){
        this.name = name;
        this.password = password;
        this.email = email;
        this.username = userName;
        this.profileDescription = profileDescription;
        this.instrument = instrument;
        this.proficiency = proficiency;
        this.address = address;
        this.records = records;
    }

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
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
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

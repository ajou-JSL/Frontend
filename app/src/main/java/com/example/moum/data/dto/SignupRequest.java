package com.example.moum.data.dto;

import com.example.moum.data.entity.Record;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SignupRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("profileDescription")
    private String profileDescription;
    @SerializedName("instrument")
    private String instrument;
    @SerializedName("proficiency")
    private String proficiency;
    @SerializedName("address")
    private String address;
    @SerializedName("records")
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

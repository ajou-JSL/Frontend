package com.example.moum.data.dto;

import com.example.moum.data.entity.Record;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SignupRequest {
    @SerializedName("username")
    private String memberId;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String nickname;

    @SerializedName("profileDescription")
    private String profileDescription;

    @SerializedName("instrument")
    private String instrument;

    @SerializedName("proficiency")
    private String proficiency;

    @SerializedName("verifyCode")
    private String verifyCode;

    @SerializedName("address")
    private String address;

    @SerializedName("records")
    private ArrayList<Record> records;

    public SignupRequest(String memberId, String password, String email, String nickname, String profileDescription, String instrument, String proficiency, String address, ArrayList<Record> records){
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileDescription = profileDescription;
        this.instrument = instrument;
        this.proficiency = proficiency;
        this.address = address;
        this.records = records;
    }

    public SignupRequest(String memberId, String password, String email, String nickname, String profileDescription, String instrument, String proficiency, String address, String verifyCode, ArrayList<Record> records){
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileDescription = profileDescription;
        this.instrument = instrument;
        this.proficiency = proficiency;
        this.address = address;
        this.verifyCode = verifyCode;
        this.records = records;
    }

    public String getMemberId() {
        return memberId;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
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

    public String getVerifyCode() {
        return verifyCode;
    }
        public ArrayList<Record> getRecords() {
        return records;
    }

    public void setMemberId(String name) {
        this.memberId = memberId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
        public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
}

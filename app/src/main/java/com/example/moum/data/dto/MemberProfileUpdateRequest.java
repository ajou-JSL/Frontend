package com.example.moum.data.dto;

import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Team;

import java.util.ArrayList;

public class MemberProfileUpdateRequest {
    private String name;
    private String username;
    private String profileDescription;
    private String email;
    private String profileImageUrl;
    private String proficiency;
    private String instrument;
    private String address;

    public MemberProfileUpdateRequest(String name, String username, String profileDescription, String email, String profileImageUrl, String proficiency, String instrument, String address) {
        this.name = name;
        this.username = username;
        this.profileDescription = profileDescription;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.proficiency = proficiency;
        this.instrument = instrument;
        this.address = address;
    }

}

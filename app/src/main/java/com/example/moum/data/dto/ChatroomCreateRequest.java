package com.example.moum.data.dto;

import java.util.ArrayList;

public class ChatroomCreateRequest {
    private String name;
    private Integer type;
    private Integer teamId;
    private Integer leaderId;
    private ArrayList<Integer> members;

    public ChatroomCreateRequest(String name, Integer type, Integer teamId, Integer leaderId, ArrayList<Integer> members) {
        this.name = name;
        this.type = type;
        this.teamId = teamId;
        this.leaderId = leaderId;
        this.members = members;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setMembers(ArrayList<Integer> members) {
        this.members = members;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getMembers() {
        return members;
    }

    public Integer getType() {
        return type;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

}

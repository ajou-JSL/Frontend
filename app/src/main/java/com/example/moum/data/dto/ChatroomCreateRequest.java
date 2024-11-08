package com.example.moum.data.dto;

import com.example.moum.data.entity.Chatroom;

import java.util.ArrayList;
import java.util.List;

public class ChatroomCreateRequest {
    private String name;
    private Chatroom.ChatroomType type;
    private Integer teamId;
    private Integer leaderId;
    private ArrayList<Member> members;

    public ChatroomCreateRequest(String name, Chatroom.ChatroomType type, Integer teamId, Integer leaderId, ArrayList<Member> members){
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

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void setType(Chatroom.ChatroomType type) {
        this.type = type;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Chatroom.ChatroomType getType() {
        return type;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public static class Member {
        private String memberId;

        public Member(String memberId){
            this.memberId = memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberId() {
            return memberId;
        }
    }
}

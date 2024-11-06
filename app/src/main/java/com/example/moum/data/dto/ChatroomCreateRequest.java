package com.example.moum.data.dto;

import com.example.moum.data.entity.Chatroom;

import java.util.ArrayList;
import java.util.List;

public class ChatroomCreateRequest {
    private Integer groupId;
    private String chatroomName;
    private Chatroom.ChatroomType chatroomType;
    private String chatroomLeader;
    private ArrayList<Member> members;

    public ChatroomCreateRequest(Integer groupId, String chatroomName, Chatroom.ChatroomType chatroomType, String chatroomLeader, ArrayList<Member> members){
        this.groupId = groupId;
        this.chatroomName = chatroomName;
        this.chatroomType = chatroomType;
        this.chatroomLeader = chatroomLeader;
        this.members = members;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public void setChatroomLeader(String chatroomLeader) {
        this.chatroomLeader = chatroomLeader;
    }

    public void setChatroomType(Chatroom.ChatroomType chatroomType) {
        this.chatroomType = chatroomType;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getChatroomLeader() {
        return chatroomLeader;
    }

    public Chatroom.ChatroomType getChatroomType() {
        return chatroomType;
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

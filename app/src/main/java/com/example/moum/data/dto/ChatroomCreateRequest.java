package com.example.moum.data.dto;

import java.util.ArrayList;
import java.util.List;

public class ChatroomCreateRequest {
    private Integer groupId;
    private String chatroomName;
    private ArrayList<Member> members;

    public ChatroomCreateRequest(Integer groupId, String chatroomName, ArrayList<Member> members){
        this.groupId = groupId;
        this.chatroomName = chatroomName;
        this.members = members;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getChatroomName() {
        return chatroomName;
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

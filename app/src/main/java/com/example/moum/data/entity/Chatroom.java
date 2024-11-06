package com.example.moum.data.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Chatroom {
    private Integer groupId;
    private String receiverId;
    private Integer chatroomId;
    private String chatroomName;
    private ChatroomType chatroomType;
    private String chatroomLeader;
    private String chatroomContent;
    private LocalDateTime chatroomLastTimestamp;
    private Uri chatroomProfile;


    public enum ChatroomType{
        PERSONAL_CHAT(0),
        MULTI_CHAT(1),
        SYSTEM_CHAT(2);

        private final int value;
        ChatroomType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public Chatroom(Integer groupId, String receiverId, Integer chatroomId, String chatroomName, ChatroomType chatroomType, String chatroomLeader){
        this.groupId = groupId;
        this.receiverId = receiverId;
        this.chatroomId = chatroomId;
        this.chatroomType = chatroomType;
        this.chatroomLeader = chatroomLeader;
    }

    public Chatroom(Integer groupId, String receiverId, Integer chatroomId, String chatroomName, String chatroomContent, LocalDateTime chatroomLastTimestamp, Uri chatroomProfile, ChatroomType chatroomType, String chatroomLeader){
        this.groupId = groupId;
        this.receiverId = receiverId;
        this.chatroomId = chatroomId;
        this.chatroomName = chatroomName;
        this.chatroomContent = chatroomContent;
        this.chatroomLastTimestamp = chatroomLastTimestamp;
        this.chatroomProfile = chatroomProfile;
        this.chatroomType = chatroomType;
        this.chatroomLeader = chatroomLeader;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public Integer getChatroomId() {
        return chatroomId;
    }

    public LocalDateTime getChatroomLastTimestamp() {
        return chatroomLastTimestamp;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public Uri getChatroomProfile() {
        return chatroomProfile;
    }

    public String getChatroomContent() {
        return chatroomContent;
    }

    public ChatroomType getChatroomType() {
        return chatroomType;
    }

    public String getChatroomLeader() {
        return chatroomLeader;
    }


    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setChatroomLeader(String chatroomLeader) {
        this.chatroomLeader = chatroomLeader;
    }

    public void setChatroomType(ChatroomType chatroomType) {
        this.chatroomType = chatroomType;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public void setChatroomId(Integer chatroomId) {
        this.chatroomId = chatroomId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setChatroomContent(String chatroomContent) {
        this.chatroomContent = chatroomContent;
    }

    public void setChatroomLastTimestamp(LocalDateTime chatroomLastTimestamp) {
        this.chatroomLastTimestamp = chatroomLastTimestamp;
    }

    public void setChatroomProfile(Uri chatroomProfile) {
        this.chatroomProfile = chatroomProfile;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "groupId=" + groupId +
                ", chatroomId=" + chatroomId +
                ", chatroomName='" + chatroomName + '\'' +
                ", chatroomContent='" + chatroomContent + '\'' +
                ", chatroomLastTimestamp=" + chatroomLastTimestamp + '\'' +
                ", chatroomProfile=" + chatroomProfile +
                '}';
    }

}

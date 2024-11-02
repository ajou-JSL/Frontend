package com.example.moum.data.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Chatroom {
    private Integer chatroomId;
    private String chatroomName;
    private String chatroomContent;
    private LocalDateTime chatroomLastTimestamp;
    private Uri chatroomProfile;
    private ChatroomType chatroomType;
    private String chatroomLeader;

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

    public Chatroom(Integer chatroomId, String chatroomName, String chatroomContent, LocalDateTime chatroomLastTimestamp, Uri chatroomProfile, ChatroomType chatroomType, String chatroomLeader){
        this.chatroomId = chatroomId;
        this.chatroomName = chatroomName;
        this.chatroomContent = chatroomContent;
        this.chatroomLastTimestamp = chatroomLastTimestamp;
        this.chatroomProfile = chatroomProfile;
        this.chatroomType = chatroomType;
        this.chatroomLeader = chatroomLeader;
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

    @Override
    public String toString() {
        return "Chatroom{" +
                "chatroomId=" + chatroomId +
                ", chatroomName='" + chatroomName + '\'' +
                ", chatroomContent='" + chatroomContent + '\'' +
                ", chatroomLastTimestamp=" + chatroomLastTimestamp + '\'' +
                ", chatroomProfile=" + chatroomProfile +
                '}';
    }

}

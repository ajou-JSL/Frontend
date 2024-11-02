package com.example.moum.data.entity;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class Chatroom {
    private Integer chatroomId;
    private String chatroomName;
    private String chatroomContent;
    private LocalDateTime chatroomLastTimestamp;
    private Uri chatroomProfile;

    public Chatroom(Integer chatroomId, String chatroomName, String chatroomContent, LocalDateTime chatroomLastTimestamp, Uri chatroomProfile){
        this.chatroomId = chatroomId;
        this.chatroomName = chatroomName;
        this.chatroomContent = chatroomContent;
        this.chatroomLastTimestamp = chatroomLastTimestamp;
        this.chatroomProfile = chatroomProfile;
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

package com.example.moum.data.entity;

import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.http.Url;

public class Chatroom {
    private Integer id;
    private String name;
    private ChatroomType type;
    private Integer teamId;
    private Integer leaderId;
    private String lastChat;
    private String lastTimestamp;
    private String fileUrl;

    public Chatroom(String name, ChatroomType type, Integer teamId, Integer leaderId) {
        this.name = name;
        this.type = type;
        this.teamId = teamId;
        this.leaderId = leaderId;
    }

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

    public Chatroom(Integer id, String name, ChatroomType type, Integer teamId, Integer leaderId, String lastChat, String lastTimestamp, String fileUrl){
        this.id = id;
        this.name = name;
        this.type = type;
        this.teamId = teamId;
        this.leaderId = leaderId;
        this.lastChat = lastChat;
        this.lastTimestamp = lastTimestamp;
        this.fileUrl = fileUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public ChatroomType getType() {
        return type;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public String getLastChat() {
        return lastChat;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public void setType(ChatroomType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", teamId=" + teamId +
                ", leaderId=" + leaderId +
                ", lastChat='" + lastChat + '\'' +
                ", lastTimestamp=" + lastTimestamp +
                ", fileUrl=" + fileUrl +
                '}';
    }
}

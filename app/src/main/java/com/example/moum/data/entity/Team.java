package com.example.moum.data.entity;

import android.net.Uri;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Team {
    private Integer teamId;
    private String leaderId;
    private String teamName;
    private String description;
    private LocalDateTime createdAt;
    private Uri fileUrl;
    private ArrayList<Member> members;

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileUrl(Uri fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public String getTeamName() {
        return teamName;
    }

    public Uri getFileUrl() {
        return fileUrl;
    }

    public static class Member{
        private Integer id;
        private String name;
        private String username;
        private String profileDescription;
        private Uri profileImageUrl;

        public void setProfileDescription(String profileDescription) {
            this.profileDescription = profileDescription;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProfileImageUrl(Uri profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfileDescription() {
            return profileDescription;
        }

        public String getName() {
            return name;
        }

        public Integer getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public Uri getProfileImageUrl() {
            return profileImageUrl;
        }
    }
}

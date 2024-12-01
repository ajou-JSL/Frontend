package com.example.moum.data.entity;

public class BoardGroupItem {
    private Integer teamId;
    private String content;
    private String writer;
    private String image;

    public void setBoardGroupItem(Integer teamId, String writer, String content,String image) {
        this.teamId = teamId;
        this.writer = writer;
        this.content = content;
        this.image = image;
    }

    // Getters
    public Integer getTeamId() {
        return teamId;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public String getImage() {
        return image;
    }

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }
}

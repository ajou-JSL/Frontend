package com.example.moum.data.entity;

public class BoardGroupItem {
    private Integer teamId;
    private String content;
    private String writer;
    private String image;
    private Integer exp;
    private String tier;

    public void setBoardGroupItem(Integer teamId, String writer, String content,String image, Integer exp, String tier) {
        this.teamId = teamId;
        this.writer = writer;
        this.content = content;
        this.image = image;
        this.exp = exp;
        this.tier = tier;
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

    public Integer getExp() {
        return exp;
    }

    public String getTier() {
        return tier;
    }
}

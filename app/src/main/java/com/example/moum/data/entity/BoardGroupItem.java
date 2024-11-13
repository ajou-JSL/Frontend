package com.example.moum.data.entity;

public class BoardGroupItem {
    private String content;
    private String writer;
    private String image;

    public void setBoardGroupItem(String writer, String content) {
        this.writer = writer;
        this.content = content;
        this.image = null;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }
}

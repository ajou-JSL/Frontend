package com.example.moum.data.entity;

public class BoardGroupItem {
    private String content;
    private String writer;
    private String image;

    public void setBoardGroupItem(String writer, String content,String image) {
        this.writer = writer;
        this.content = content;
        this.image = image;
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

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }
}

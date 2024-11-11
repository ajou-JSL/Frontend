package com.example.moum.data.entity;

public class BoardFreeItem {
    private String title;
    private String content;
    private String writer;
    private String time;
    private int image;

    public void setBoardFreeItem(String title, String content, String writer, String time){
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.time = time;
    }

    public void setImage(int image){
        this.image = image;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public String getTime(){
        return time;
    }

    public int getImage(){
        return image;
    }

    public boolean hasImage() {
        return image != 0;
    }
}
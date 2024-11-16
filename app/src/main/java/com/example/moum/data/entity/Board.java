package com.example.moum.data.entity;

import java.util.ArrayList;

public class Board {
    private Integer id;
    private String title;
    private String category;
    private String content;
    private String writer;
    private ArrayList<String> comments;
    private Integer commentCount;
    private Integer viewcount;
    private Integer likeCount;

    public Board(Integer id, String title, String category, String content, String writer, ArrayList<String> comments, Integer commentCount, Integer viewcount, Integer likeCount){
      this.id = id;
      this.title = title;
      this.category = category;
      this.content = content;
      this.writer = writer;
      this.comments = comments;
      this.commentCount = commentCount;
      this.viewcount = viewcount;
      this.likeCount = likeCount;
    }

    //setter
    public void setId(Integer id){ this.id = id; }
    public void setTitle(String title){ this.title = title; }
    public void setCategory(String category){ this.category = category; }
    public void setContent(String content){ this.content = content; }
    public void setWriter(String writer){ this.writer = writer; }
    public void setComments(ArrayList<String> comments){ this.comments = comments; }

    //getter
    public Integer getId(){ return id; }
    public String getTitle(){ return title; }
    public String getCategory(){ return category; }
    public String getContent(){ return content; }
    public String getWriter(){ return writer; }
    public ArrayList<String> getComments(){ return comments; }
    public Integer getCommentCount(){ return commentCount; }
    public Integer getViewcount(){ return viewcount; }
    public Integer getLikeCount(){ return likeCount; }

  
}

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

  
}

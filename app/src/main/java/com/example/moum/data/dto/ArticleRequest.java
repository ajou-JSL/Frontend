package com.example.moum.data.dto;

import java.util.ArrayList;

public class ArticleRequest {
    private Integer articleId;
    private String title;
    private String category;
    private String content;
    private Integer viewcounts;
    private Integer commentCounts;
    private Integer likeCounts;
    private String author;
    private String createAt;
    private ArrayList<String> comments;

    public ArticleRequest(Integer articleId, String title, String category, String content, Integer viewcounts, Integer commentCounts, Integer likeCounts, String author,  ArrayList<String> comments, String createAt) {
        this.articleId = articleId;
        this.title = title;
        this.category = category;
        this.content = content;
        this.viewcounts = viewcounts;
        this.commentCounts = commentCounts;
        this.likeCounts = likeCounts;
        this.author = author;
        this.comments = comments;
        this.createAt = createAt;
    }
}

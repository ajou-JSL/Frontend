package com.example.moum.data.dto;

public class ArticleRequest {
    private String title;
    private String content;
    private String category;
    private Integer genre;

    public ArticleRequest(String title, String content, String category, Integer genre) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.genre = genre;
    }
}

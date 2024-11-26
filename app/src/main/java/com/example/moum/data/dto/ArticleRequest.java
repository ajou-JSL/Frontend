package com.example.moum.data.dto;

import java.util.ArrayList;
import java.util.List;

public class ArticleRequest {
    private String title;
    private String content;
    private String category;
    private Integer gerne;

    public ArticleRequest( String title, String category, String content, Integer genre) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.gerne = genre;
    }
}

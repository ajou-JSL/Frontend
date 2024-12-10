package com.example.moum.data.dto;

public class ArticleFilterRequest {
    private String keyword;
    private boolean filterByLikesCount;
    private boolean filterByViewCount;
    private boolean filterByCommentsCount;
    private boolean filterByCreatedAt;
    private String createdAt;
    private String category;
    private String genre;

    public ArticleFilterRequest(
            String keyword,
            boolean filterByLikesCount,
            boolean filterByViewCount,
            boolean filterByCommentsCount,
            boolean filterByCreatedAt,
            String createdAt,
            String category,
            String genre) {

        this.keyword = keyword;
        this.filterByLikesCount = filterByLikesCount;
        this.filterByViewCount = filterByViewCount;
        this.filterByCommentsCount = filterByCommentsCount;
        this.filterByCreatedAt = filterByCreatedAt;
        this.createdAt = createdAt;
        this.category = category;
        this.genre = genre;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setFilterByLikesCount(boolean filterByLikesCount) {
        this.filterByLikesCount = filterByLikesCount;
    }

    public void setFilterByViewCount(boolean filterByViewCount) {
        this.filterByViewCount = filterByViewCount;
    }

    public void setFilterByCommentsCount(boolean filterByCommentsCount) {
        this.filterByCommentsCount = filterByCommentsCount;
    }

    public void setFilterByCreatedAt(boolean filterByCreatedAt) {
        this.filterByCreatedAt = filterByCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKeyword(){ return keyword;}

    public boolean getLike() { return filterByLikesCount; }
    public boolean getView() { return filterByViewCount; }
    public boolean getComment() { return filterByCommentsCount; }
    public boolean getCreatedAt() { return filterByCreatedAt; }
    public String getCreatedAtValue() {return createdAt; }
    public String getCategory() { return category; }
    public String getGenre() { return genre; }




}

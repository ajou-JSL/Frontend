package com.example.moum.data.dto;

public class ArticleFilterRequest {
    private String keyword;
    private boolean filterByLikesCount;
    private boolean filterByViewCount;
    private boolean filterByCommentsCount;
    private boolean filterByCreatedAt;
    private String createdAt;
    private Integer category;
    private Integer genre;

    public ArticleFilterRequest(
            String keyword,
            boolean filterByLikesCount,
            boolean filterByViewCount,
            boolean filterByCommentsCount,
            boolean filterByCreatedAt,
            String createdAt,
            Integer category,
            Integer genre) {

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

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

}

package com.example.moum.data.entity;

import java.util.List;

public class Article {
    private Integer id;
    private Integer authorId;
    private String title;
    private String category; //TODO enum으로 리팩토링
    private String content;
    private Integer viewCounts;
    private Integer commentsCounts;
    private Integer genres;
    private Integer likeCounts;
    private List<String> fileURL;
    private String author;
    private String authorName; // 닉네임 추가
    private List<Comment> comments;
    private String createdAt;
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Integer getCommentsCounts() {
        return commentsCounts;
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public Integer getViewCounts() {
        return viewCounts;
    }

    public Integer getGenre() {
        return genres;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getCreateAt() {
        return createdAt;
    }

    public String getUpdateAt() {
        return updatedAt;
    }

    public List<String> getFileURL() {
        return fileURL;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCommentsCounts(Integer commentCounts) {
        this.commentsCounts = commentCounts;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikeCounts(Integer likeCounts) {
        this.likeCounts = likeCounts;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewCounts(Integer viewCounts) {
        this.viewCounts = viewCounts;
    }

    public void setCreateAt(String createAt) {
        this.createdAt = createAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updatedAt = updateAt;
    }

    public void setFileURL(List<String> fileURL) {
        this.fileURL = fileURL;
    }

    public void setGenre(Integer genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                "authorId=" + authorId +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                ", viewCounts=" + viewCounts +
                ", commentCounts=" + commentsCounts +
                ", likeCounts=" + likeCounts +
                ", imageURL=" + fileURL + '\'' +
                ", author='" + author + '\'' +
                ", comments=" + comments +
                ", createdAt='" + createdAt + '\'' +
                ", viewCounts=" + updatedAt +
                '}';
    }


}

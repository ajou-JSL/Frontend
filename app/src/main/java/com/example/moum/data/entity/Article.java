package com.example.moum.data.entity;

import java.util.ArrayList;

public class Article {
    private Integer id;
    private String title;
    private String category; //TODO enum으로 리팩토링
    private String content;
    private Integer viewCounts;
    private Integer commentCounts;
    private Integer likeCounts;
    private String imageURL;
    private String author;
    private ArrayList<Comment> comments;
    private String createAt;
    private String updateAt;

    public Integer getId() {
        return id;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public Integer getCommentCounts() {
        return commentCounts;
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public Integer getViewCounts() {
        return viewCounts;
    }

    public String getAuthor() {
        return author;
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
        return createAt;
    }

    public String getUpdateAt() { return updateAt; }

    public String getImageURL() { return imageURL; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCommentCounts(Integer commentCounts) {
        this.commentCounts = commentCounts;
    }

    public void setComments(ArrayList<Comment> comments) {
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
        this.createAt = createAt;
    }

    public void setUpdateAt(String updateAt) { this.updateAt = updateAt; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                ", viewCounts=" + viewCounts +
                ", commentCounts=" + commentCounts +
                ", likeCounts=" + likeCounts +
                ", imageURL=" + imageURL + '\'' +
                ", author='" + author + '\'' +
                ", comments=" + comments +
                ", createAt='" + createAt + '\'' +
                ", viewCounts=" + updateAt +
                '}';
    }
}

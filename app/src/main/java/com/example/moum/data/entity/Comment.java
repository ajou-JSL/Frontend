package com.example.moum.data.entity;

public class Comment {
    private Integer commentId;
    private Integer articleDetailsId;
    private Integer authorId;
    private String author;
    private String content;
    private String createdAt;
    private String authorProfileImage;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Integer getArticleDetailsId() {
        return articleDetailsId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public String getCreateAt() {
        return createdAt;
    }

    public Integer getAuthorId() {return authorId;}


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setArticleDetailsId(Integer articleDetailsId) {
        this.articleDetailsId = articleDetailsId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public void setCreateAt(String createAt) {
        this.createdAt = createAt;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setAuthorProfileImage(String authorProfileImage) {
        this.authorProfileImage = authorProfileImage;
    }

    public String getAuthorProfileImage(){
        return authorProfileImage;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", articleDetailsId=" + articleDetailsId +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", authorProfileImage='" + authorProfileImage + '\'' +
                ", content='" + content + '\'' +
                ", createAt='" + createdAt + '\'' +
                '}';
    }
}
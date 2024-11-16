package com.example.moum.data.entity;

public class Comment {
    private Integer commentId;
    private Integer articleDetailsId;
    private String author;
    private String content;
    private String createAt;

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
        return createAt;
    }

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
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", articleDetailsId=" + articleDetailsId +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}
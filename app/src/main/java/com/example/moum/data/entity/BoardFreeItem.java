package com.example.moum.data.entity;

public class BoardFreeItem {
    private Integer postId;
    private String title;
    private String content;
    private String writer;
    private String time;
    private String image;
    private Integer commentsCounts;
    private Integer viewCounts;

    public void setBoardFreeItem(Integer postId, String title, String writer, String time, Integer commentCounts, Integer viewCounts) {
        this.postId = postId;
        this.title = title;
        this.writer = writer;
        this.time = time;
        this.commentsCounts = commentCounts;
        this.viewCounts = viewCounts;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getBoardId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getCommentsCounts() {
        return commentsCounts;
    }

    public Integer getViewCounts() {
        return viewCounts;
    }

    public String getWriter() {
        return writer;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public void setCommentCounts(Integer commentCounts) {
        this.commentsCounts = commentCounts;
    }

    public void setViewCounts(Integer viewCounts) {
        this.viewCounts = viewCounts;
    }

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }
}

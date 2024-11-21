package com.example.moum.data.entity;

public class Like {
    private Integer likesId;
    private Integer memberId;
    private Integer articlesId;

    public Integer getArticlesId() {
        return articlesId;
    }

    public Integer getLikesId() {
        return likesId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public void setArticlesId(Integer articlesId) {
        this.articlesId = articlesId;
    }

    public void setLikesId(Integer likesId) {
        this.likesId = likesId;
    }

    @Override
    public String toString() {
        return "Like{" +
                "likesId=" + likesId +
                ", memberId=" + memberId +
                ", articlesId=" + articlesId +
                '}';
    }
}
package com.example.moum.data.entity;

import androidx.annotation.NonNull;

public class Like {
    private Integer likesCount;
    private boolean liked;


    public Integer getLikesCount() {
        return likesCount;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }
    public void getLiked(boolean liked) {
        this.liked = liked;
    }

    @NonNull
    @Override
    public String toString() {
        return "Like{" +
                "likesCount=" + likesCount +
                ", liked=" + liked +
                '}';
    }
}
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

    @NonNull
    @Override
    public String toString() {
        return "Like{" +
                "likesCount=" + likesCount +
                ", liked=" + liked +
                '}';
    }
}
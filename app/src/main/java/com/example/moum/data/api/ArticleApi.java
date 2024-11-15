package com.example.moum.data.api;

import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Chat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleApi {
    @GET("/api/articles/hot")
    Call<SuccessResponse<List<Article>>> loadArticlesHot(
        @Query("page") Integer page,
        @Query("size") Integer size
    );
}

package com.example.moum.data.api;

import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Performance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PerformApi {
    @GET("/api/performs-all/this-month")
    Call<SuccessResponse<List<Performance>>> loadPerformsHot(
            @Query("page") Integer page,
            @Query("size") Integer size
    );
}

package com.example.moum.data.api;

import com.example.moum.data.dto.MoumRequest;
import com.example.moum.data.dto.PerformRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Content;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Performance;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PerformApi {
    @Multipart
    @POST("/api/performs")
    Call<SuccessResponse<Performance>> createPerform(
            @Part MultipartBody.Part file,
            @Part("requestDto") PerformRequest performRequest
    );

    @Multipart
    @PATCH("/api/performs/{performId}")
    Call<SuccessResponse<Performance>> updatePerform(
            @Path("performId") Integer performId,
            @Part MultipartBody.Part file,
            @Part("requestDto") PerformRequest performRequest
    );

    @DELETE("/api/performs/{performId}")
    Call<SuccessResponse<Performance>> deletePerform(
            @Path("performId") Integer performId
    );

    @GET("/api/performs/{performId}")
    Call<SuccessResponse<Performance>> loadPerform(
            @Path("performId") Integer performId
    );

    @GET("/api/performs-all")
    Call<SuccessResponse<List<Performance>>> loadPerforms(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @GET("/api/performs-all/this-month")
    Call<SuccessResponse<Content<List<Performance>>>> loadPerformsHot(
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @GET("/api/performs")
    Call<SuccessResponse<Performance>> loadPerformOfMoum(
            @Query("moumId") Integer moumId
    );
}

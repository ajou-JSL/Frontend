package com.example.moum.data.api;

import com.example.moum.data.dto.SuccessResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PromoteApi {
    @POST("/api/pamphlet/qr-code")
    Call<SuccessResponse<String>> createQr(
            @Query("id") Integer performId
    );

    @GET("/api/pamphlet/qr-code/{performId}")
    Call<SuccessResponse<String>> loadQr(
            @Path("performId") Integer performId
    );

    @DELETE("/api/pamphlet/qr-code")
    Call<SuccessResponse<String>> deleteQr(
            @Query("id") Integer performId
    );

}

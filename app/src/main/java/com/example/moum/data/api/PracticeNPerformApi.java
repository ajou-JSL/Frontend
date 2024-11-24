package com.example.moum.data.api;

import com.example.moum.data.dto.PerformRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PracticeNPerformApi {
    @GET("/api/business/practice-room/view/{practiceroomId}")
    Call<SuccessResponse<Practiceroom>> getPracticeroom(
           @Path("practiceroomId") Integer practiceroomId
    );

    @GET("/api/business/practice-rooms")
    Call<SuccessResponse<List<Practiceroom>>> getPracticerooms(
            @Query("page") Integer page,
            @Query("size") Integer size
    );


    @GET("/api/business/performance-hall/view/{performHallId}")
    Call<SuccessResponse<PerformanceHall>> getPerformHall(
            @Path("performHallId") Integer performHallId
    );

    @GET("/api/business/performance-halls")
    Call<SuccessResponse<List<PerformanceHall>>> getPerformHalls(
            @Query("page") Integer page,
            @Query("size") Integer size
    );
}

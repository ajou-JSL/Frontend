package com.example.moum.data.api;

import com.example.moum.data.dto.PerformMoumRequest;
import com.example.moum.data.dto.PerformRequest;
import com.example.moum.data.dto.PracticeMoumRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Content;
import com.example.moum.data.entity.MoumPerformHall;
import com.example.moum.data.entity.MoumPracticeroom;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PracticeNPerformApi {
    @GET("/api/business/practice-room/view/{practiceroomId}")
    Call<SuccessResponse<Practiceroom>> getPracticeroom(
            @Path("practiceroomId") Integer practiceroomId
    );

    @GET("/api/business/practice-rooms")
    Call<SuccessResponse<Content<List<Practiceroom>>>> getPracticerooms(
            @Query("page") Integer page,
            @Query("size") Integer size
    );


    @GET("/api/business/performance-hall/view/{performHallId}")
    Call<SuccessResponse<PerformanceHall>> getPerformHall(
            @Path("performHallId") Integer performHallId
    );

    @GET("/api/business/performance-halls")
    Call<SuccessResponse<Content<List<PerformanceHall>>>> getPerformHalls(
            @Query("page") Integer page,
            @Query("size") Integer size
    );


    /**
     * 아래는 "모음 내에서의" 연습실 및 공연장 관련 API
     */
    @POST("/api/moum/practice-room")
    Call<SuccessResponse<MoumPracticeroom>> createPracticeroomOfMoum(
            @Body PracticeMoumRequest practiceMoumRequest
    );

    @DELETE("/api/moum/{moumId}/practice-room/{moumPracticeroomId}")
    Call<SuccessResponse<List<String>>> deletePracticeroomOfMoum(
            @Path("moumId") Integer moumId,
            @Path("moumPracticeroomId") Integer moumPracticeroomId
    );

    @GET("/api/moum/{moumId}/practice-room")
    Call<SuccessResponse<List<MoumPracticeroom>>> getPracticeroomsOfMoum(
            @Path("moumId") Integer moumId
    );

    @POST("/api/moum/performance-hall")
    Call<SuccessResponse<MoumPerformHall>> createPerformHallOfMoum(
            @Body PerformMoumRequest performMoumRequest
    );

    @POST("/api/moum/{moumId}/performance-hall/{performHallId}")
    Call<SuccessResponse<List<String>>> deletePerformHallOfMoum(
            @Path("moumId") Integer moumId,
            @Path("performHallId") Integer performHallId
    );

    @GET("/api/moum/{moumId}/performance-hall")
    Call<SuccessResponse<List<MoumPerformHall>>> getPerformHallsOfMoum(
            @Path("moumId") Integer moumId
    );

}

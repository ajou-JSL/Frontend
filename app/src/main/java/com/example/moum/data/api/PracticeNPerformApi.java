package com.example.moum.data.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @GET("/api/business/practice-rooms/search")
    Call<SuccessResponse<Content<List<Practiceroom>>>> searchPracticerooms(
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("sortBy") String sortBy,
            @Query("orderBy") String orderBy,
            @Query("name") String name,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude,
            @Query("minPrice") Integer minPrice,
            @Query("maxPrice") Integer maxPrice,
            @Query("minCapacity") Integer minCapacity,
            @Query("maxCapacity") Integer maxCapacity,
            @Query("type") Integer type,
            @Query("minStand") Integer minStand,
            @Query("maxStand") Integer maxStand,
            @Query("hasPiano") Boolean hasPiano,
            @Query("hasAmp") Boolean hasAmp,
            @Query("hasSpeaker") Boolean hasSpeaker,
            @Query("hasMic") Boolean hasMic,
            @Query("hasDrums") Boolean hasDrums
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

    @GET("/api/business/performance-halls/search")
    Call<SuccessResponse<Content<List<PerformanceHall>>>> searchPerformHalls(
            @NonNull @Query("page") Integer page,
            @NonNull @Query("size") Integer size,
            @NonNull @Query("sortBy") String sortBy,
            @NonNull @Query("orderBy") String orderBy,
            @Nullable @Query("name") String name,
            @Nullable @Query("latitude") Double latitude,
            @Nullable @Query("longitude") Double longitude,
            @Nullable @Query("minPrice") Integer minPrice,
            @Nullable @Query("maxPrice") Integer maxPrice,
            @Nullable @Query("maxHallSize") Integer maxHallSize,
            @Nullable @Query("minHallSize") Integer minHallSize,
            @Nullable @Query("minCapacity") Integer minCapacity,
            @Nullable @Query("maxCapacity") Integer maxCapacity,
            @Nullable @Query("minStand") Integer minStand,
            @Nullable @Query("maxStand") Integer maxStand,
            @Nullable @Query("hasPiano") Boolean hasPiano,
            @Nullable @Query("hasAmp") Boolean hasAmp,
            @Nullable @Query("hasSpeaker") Boolean hasSpeaker,
            @Nullable @Query("hasMic") Boolean hasMic,
            @Nullable @Query("hasDrums") Boolean hasDrums
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

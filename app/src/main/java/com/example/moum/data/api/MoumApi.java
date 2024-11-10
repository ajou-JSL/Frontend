package com.example.moum.data.api;

import com.example.moum.data.dto.MoumRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.dto.TeamRequest;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;

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

public interface MoumApi {
    @Multipart
    @POST("/api/moum")
    Call<SuccessResponse<Moum>> createMoum(
            @Part("file") MultipartBody.Part file,
            @Part("lifecycleRequestDto") MoumRequest moumRequest
    );

    @GET("/api/moum/{moumId}")
    Call<SuccessResponse<Moum>> loadMoum(
            @Path("moumId") Integer moumId
    );

    @GET("/api/moum/{teamId}")
    Call<SuccessResponse<List<Moum>>> loadMoumsOfTeam(
            @Path("teamId") Integer teamId
    );

    @GET("/api/moum/mylist")
    Call<SuccessResponse<List<Moum>>> loadMoumsAll(
    );


}

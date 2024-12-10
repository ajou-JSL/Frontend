package com.example.moum.data.api;

import com.example.moum.data.dto.MoumProcessRequest;
import com.example.moum.data.dto.MoumRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Moum;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
            @Part List<MultipartBody.Part> file,
            @Part("lifecycleRequestDto") MoumRequest moumRequest
    );

    @GET("/api/moum/{moumId}")
    Call<SuccessResponse<Moum>> loadMoum(
            @Path("moumId") Integer moumId
    );

    @GET("/api/moum-all/team/{teamId}")
    Call<SuccessResponse<List<Moum>>> loadMoumsOfTeam(
            @Path("teamId") Integer teamId
    );

    @GET("/api/moum-all/my")
    Call<SuccessResponse<List<Moum>>> loadMoumsOfMe(
    );

    @Multipart
    @PATCH("/api/moum/{moumId}")
    Call<SuccessResponse<Moum>> updateMoum(
            @Path("moumId") Integer moumId,
            @Part List<MultipartBody.Part> file,
            @Part("lifecycleRequestDto") MoumRequest moumRequest
    );

    @DELETE("/api/moum/{moumId}")
    Call<SuccessResponse<Moum>> deleteMoum(
            @Path("moumId") Integer moumId
    );

    @PATCH("/api/moum/finish/{moumId}")
    Call<SuccessResponse<Moum>> finishMoum(
            @Path("moumId") Integer moumId

    );

    @PATCH("/api/moum/reopen/{moumId}")
    Call<SuccessResponse<Moum>> reopenMoum(
            @Path("moumId") Integer moumId

    );

    @PATCH("api/moum/update-process/{moumId}")
    Call<SuccessResponse<Moum>> updateProcessMoum(
            @Path("moumId") Integer moumId,
            @Body MoumProcessRequest moumProcessRequest
    );

}

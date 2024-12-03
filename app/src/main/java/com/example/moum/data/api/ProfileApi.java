package com.example.moum.data.api;

import com.example.moum.data.dto.MemberProfileRankResponse;
import com.example.moum.data.dto.MemberProfileUpdateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Content;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProfileApi {
    @GET("/api/profiles/{memberId}")
    Call<SuccessResponse<Member>> loadMemberProfile(
            @Path("memberId") Integer memberId
    );

    @Multipart
    @PATCH("/api/profiles/{memberId}")
    Call<SuccessResponse<Member>> updateMemberProfile(
            @Path("memberId") Integer memberId,
            @Part MultipartBody.Part file,
            @Part("updateProfileDto") MemberProfileUpdateRequest memberProfileUpdateRequest
            );

    @GET("/api/profiles-all/rank")
    Call<SuccessResponse<Content<List<MemberProfileRankResponse>>>> loadMembersByRank(
            @Query("page") Integer page,
            @Query("size") Integer size
    );
}

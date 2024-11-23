package com.example.moum.data.api;

import com.example.moum.data.dto.MemberProfileUpdateRequest;
import com.example.moum.data.dto.SuccessResponse;
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
}

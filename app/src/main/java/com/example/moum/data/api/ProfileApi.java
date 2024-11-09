package com.example.moum.data.api;

import com.example.moum.data.dto.MemberProfileUpdateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProfileApi {
    @POST("/api/profiles/{memberId}")
    Call<SuccessResponse<Member>> loadMemberProfile(
            @Path("memberId") Integer memberId
    );

    @PATCH("/api/profiles/{memberId}")
    Call<SuccessResponse<Member>> updateMemberProfile(
            @Path("memberId") Integer memberId,
            @Body MemberProfileUpdateRequest memberProfileUpdateRequest
            );
}

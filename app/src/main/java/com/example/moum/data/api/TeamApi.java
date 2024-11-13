package com.example.moum.data.api;

import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.dto.TeamRequest;
import com.example.moum.data.entity.Member;
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

public interface TeamApi {
    @Multipart
    @POST("/api/teams")
    Call<SuccessResponse<Team>> createTeam(
            @Part MultipartBody.Part file,
            @Part("teamRequestDto") TeamRequest teamRequest
    );

    @Multipart
    @PATCH("/api/teams/{teamId}")
    Call<SuccessResponse<Team>> updateTeam(
            @Path("teamId") Integer teamId,
            @Part MultipartBody.Part file,
            @Part("teamRequestDto") TeamRequest teamRequest
    );

    @DELETE("/api/teams/{teamId}")
    Call<SuccessResponse<Team>> deleteTeam(
            @Path("teamId") Integer teamId
    );


    @GET("/api/teams/{teamId}")
    Call<SuccessResponse<Team>> loadTeam(
            @Path("teamId") Integer teamId
    );

    @GET("/api/teams-all")
    Call<SuccessResponse<List<Team>>> loadTeams();

    @GET("/api/teams-all/{memberId}")
    Call<SuccessResponse<List<Team>>> loadTeamsAsMember(
            @Path("memberId") Integer memberId
    );

    @DELETE("/api/teams/{teamId}/kick/{memberId}")
    Call<SuccessResponse<Team>> kickMemberFromTeam(
            @Path("teamId") Integer teamId,
            @Path("memberId") Integer memberId
    );

    @DELETE("/api/teams/leave/{teamId}")
    Call<SuccessResponse<Team>> leaveTeam(
            @Path("teamId") Integer teamId
    );

    @POST("/api/teams/{teamId}/invite/{memberId}")
    Call<SuccessResponse<Member>> inviteMemberToTeam(
            @Path("teamId") Integer teamId,
            @Path("memberId") Integer memberId
    );
}

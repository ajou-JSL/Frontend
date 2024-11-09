package com.example.moum.data.api;

import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TeamApi {
    @GET("/api/teams/{teamId}")
    Call<SuccessResponse<Team>> loadTeam(
            @Path("teamId") Integer teamId
    );

    @GET("/api/teams-all")
    Call<SuccessResponse<List<Team>>> loadTeams();

    @GET("/api/teams-all/{memberId}")
    Call<SuccessResponse<List<Team>>> loadTeamsAsLeader(
            @Path("memberId") Integer id
    );

//    @POST("/api/teams/teams-all/{memberId}")
//    Call<SuccessResponse<List<Team>>> loadTeamsAsMember(
//            @Path("memberId") String memberId
//    );

}

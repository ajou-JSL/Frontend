package com.example.moum.data.api;

import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Group;
import com.example.moum.data.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TeamApi {
    @POST("/api/teams/teams-all/{memberId}")
    Call<SuccessResponse<List<Group>>> loadGroups(
            @Path("memberId") String memberId
    );

    @POST("api/teams/{groupId}")
    Call<SuccessResponse<List<User>>> loadMembersOfGroup(
            @Path(("groupId")) Integer groupId
    );
}

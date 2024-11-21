package com.example.moum.data.api;

import com.example.moum.data.dto.EmailCodeRequest;
import com.example.moum.data.dto.ReportRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportApi {
    @POST("/api/report/member/{memberId}")
    Call<SuccessResponse<ReportMember>> reportMember(
            @Path("memberId") Integer memberId,
            @Body ReportRequest reportRequest
    );

    @GET("/api/report/member/view/{reportId}")
    Call<SuccessResponse<ReportMember>> loadReportMember(
            @Path("reportId") Integer reportId
    );

    @POST("/api/report/team/{teamId}")
    Call<SuccessResponse<ReportTeam>> reportTeam(
            @Path("teamId") Integer teamId,
            @Body ReportRequest reportRequest
    );

    @GET("/api/report/team/view/{reportId}")
    Call<SuccessResponse<ReportTeam>> loadReportTeam(
            @Path("reportId") Integer reportId
    );

    @POST("/api/report/article/{articleId}")
    Call<SuccessResponse<ReportArticle>> reportArticle(
            @Path("articleId") Integer articleId,
            @Body ReportRequest reportRequest
    );

    @GET("/api/report/article/view/{reportId}")
    Call<SuccessResponse<ReportArticle>> loadReportArticle(
            @Path("reportId") Integer reportId
    );

}

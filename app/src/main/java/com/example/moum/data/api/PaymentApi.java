package com.example.moum.data.api;

import com.example.moum.data.dto.SettlementRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Settlement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentApi {
    @POST("/api/moums/{moumId}/settlements")
    Call<SuccessResponse<Settlement>> createSettlement(
            @Path("moumId") Integer moumId,
            @Body SettlementRequest settlementRequest
    );

    @GET("/api/moums/{moumId}/settlements-all")
    Call<SuccessResponse<List<Settlement>>> loadSettlements(
            @Path("moumId") Integer moumId
    );

    @DELETE("/api/moums/{moumId}/settlements/{settlementId}")
    Call<SuccessResponse<Settlement>> deleteSettlement(
            @Path("moumId") Integer moumId,
            @Path("settlementId") Integer settlementId
    );
}

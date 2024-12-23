package com.example.moum.data.api;

import com.example.moum.data.dto.RefreshRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Member;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginApi {
    @Multipart
    @POST("/login")
    Call<SuccessResponse<Member>> login(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    @POST("/logout")
    Call<SuccessResponse<Member>> logout(
    );

    @POST("/reissue")
    Call<SuccessResponse<String>> refresh(
            @Body RefreshRequest refreshRequest
    );
}

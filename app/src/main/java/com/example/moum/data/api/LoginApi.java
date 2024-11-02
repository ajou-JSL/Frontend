package com.example.moum.data.api;

import com.example.moum.data.dto.LoginRequest;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SuccessResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LoginApi {
    @Multipart
    @POST("/login")
    Call<SuccessResponse> login(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );
}

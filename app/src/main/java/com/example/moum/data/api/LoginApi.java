package com.example.moum.data.api;

import com.example.moum.data.dto.EmailAuthRequest;
import com.example.moum.data.dto.EmailAuthResponse;
import com.example.moum.data.dto.LoginRequest;
import com.example.moum.data.dto.SuccessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("/login")
    Call<SuccessResponse> login(
            @Body LoginRequest loginRequest
    );
}

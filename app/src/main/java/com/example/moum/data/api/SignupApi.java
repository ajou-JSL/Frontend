package com.example.moum.data.api;

import com.example.moum.data.entity.EmailAuthRequest;
import com.example.moum.data.entity.EmailAuthResponse;
import com.example.moum.data.entity.SignupResponse;
import com.example.moum.data.entity.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignupApi {

    @POST("/api/send-email")
    Call<EmailAuthResponse> emailAuth(
            @Body EmailAuthRequest emailAuthRequest
    );

    @POST("/signup")
    Call<SignupResponse> signup(
            @Body SignupRequest signupRequest
    );
}

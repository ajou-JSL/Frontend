package com.example.moum.data.api;

import com.example.moum.data.dto.EmailAuthRequest;
import com.example.moum.data.dto.EmailAuthResponse;
import com.example.moum.data.dto.EmailCodeRequest;
import com.example.moum.data.dto.EmailCodeResponse;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SignupResponse;
import com.example.moum.data.entity.User;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SignupApi {

    @POST("/send-email")
    Call<EmailAuthResponse> emailAuth(
            @Body EmailAuthRequest emailAuthRequest
    );

    @POST("/verify-code")
    Call<EmailCodeResponse> checkEmailCode(
            @Body EmailCodeRequest emailCodeRequest
    );

    @Multipart
    @POST("/signup")
    Call<SignupResponse> signup(
            @Part("profile") SignupRequest signupRequest,
            @Part MultipartBody.Part profileImage

    );
}

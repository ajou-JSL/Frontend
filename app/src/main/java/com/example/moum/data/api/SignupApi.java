package com.example.moum.data.api;

import com.example.moum.data.dto.EmailAuthRequest;
import com.example.moum.data.dto.EmailCodeRequest;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SuccessResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SignupApi {

    @POST("/send-email")
    Call<SuccessResponse> emailAuth(
            @Body EmailAuthRequest emailAuthRequest
    );

    @POST("/verify-code")
    Call<SuccessResponse> checkEmailCode(
            @Body EmailCodeRequest emailCodeRequest
    );

    @Multipart
    @POST("/signup")
    Call<SuccessResponse> signup(
            @Part("memberRequestDto") SignupRequest signupRequest,
            @Part MultipartBody.Part profileImage

    );
}

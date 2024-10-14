package com.example.moum.repository;

import android.util.Pair;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.entity.EmailAuthRequest;
import com.example.moum.data.entity.EmailAuthResponse;
import com.example.moum.data.entity.EmailCodeRequest;
import com.example.moum.data.entity.EmailCodeResponse;
import com.example.moum.utils.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRepository {
    private static SignupRepository instance;
    private SignupApi signupApi;
    private Retrofit retrofitClient;

    private SignupRepository() {
        retrofitClient = RetrofitClient.getClient();
        signupApi = retrofitClient.create(SignupApi.class);
    }

    public static SignupRepository getInstance() {
        if (instance == null) {
            instance = new SignupRepository();
        }
        return instance;
    }

    public void emailAuth(String email, com.example.moum.utils.Callback<Validation> callback) {

        EmailAuthRequest emailAuthRequest = new EmailAuthRequest(email);
        Call<EmailAuthResponse> result = signupApi.emailAuth(emailAuthRequest);
        result.enqueue(new Callback<EmailAuthResponse>() {
            @Override
            public void onResponse(Call<EmailAuthResponse> call, Response<EmailAuthResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResult(Validation.VALID_ALL);
                } else {
                    callback.onResult(Validation.EMAIL_AUTH_FAILED);
                }
            }

            @Override
            public void onFailure(Call<EmailAuthResponse> call, Throwable t) {
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });

    }

    public void checkEmailCode(String emailCode, com.example.moum.utils.Callback<Validation> callback) {

        EmailCodeRequest emailCodeRequest = new EmailCodeRequest(emailCode);
        Call<EmailCodeResponse> result = signupApi.checkEmailCode(emailCodeRequest);
        result.enqueue(new Callback<EmailCodeResponse>() {
            @Override
            public void onResponse(Call<EmailCodeResponse> call, Response<EmailCodeResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResult(Validation.VALID_ALL);
                } else {
                    callback.onResult(Validation.EMAIL_CODE_FAILED);
                    //TO-DO
                }
            }

            @Override
            public void onFailure(Call<EmailCodeResponse> call, Throwable t) {
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });
    }
}

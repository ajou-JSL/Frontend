package com.example.moum.repository;

import android.util.Pair;

import com.example.moum.data.api.SignupApi;

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

    public void emailAuth(String email, com.example.moum.repository.Callback<Pair<Boolean, String>> callback) {

        Call<SignupApi.EmailAuthResponse> result = signupApi.emailAuth(email);
        result.enqueue(new Callback<SignupApi.EmailAuthResponse>() {
            @Override
            public void onResponse(Call<SignupApi.EmailAuthResponse> call, Response<SignupApi.EmailAuthResponse> response) {
                if (response.isSuccessful()) {
                    callback.onResult(new Pair<Boolean,String>(true, "이메일 인증 성공"));
                } else {
                    callback.onResult(new Pair<Boolean,String>(false, "이메일 인증 실패"));
                }
            }

            @Override
            public void onFailure(Call<SignupApi.EmailAuthResponse> call, Throwable t) {
                callback.onResult(new Pair<Boolean,String>(false, "네트워크 연결 실패"));
            }
        });

    }
}

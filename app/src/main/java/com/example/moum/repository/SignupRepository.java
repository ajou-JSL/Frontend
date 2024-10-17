package com.example.moum.repository;

import android.util.Log;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.entity.EmailAuthRequest;
import com.example.moum.data.entity.EmailAuthResponse;
import com.example.moum.data.entity.EmailCodeRequest;
import com.example.moum.data.entity.EmailCodeResponse;
import com.example.moum.data.entity.ErrorDetail;
import com.example.moum.data.entity.ErrorResponse;
import com.example.moum.utils.ServerCodeMap;
import com.example.moum.utils.Validation;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRepository {
    private static SignupRepository instance;
    private SignupApi signupApi;
    private Retrofit retrofitClient;
    private String TAG = getClass().toString();

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

                    /*성공적으로 응답을 받았을 때*/
                    EmailAuthResponse responseBody = response.body();
                    Log.d(TAG, "status: " + responseBody.getStatus() + "code: " + responseBody.getCode() + "message: " + responseBody.getMessage() + "data: " + responseBody.getData());
                    Validation validation = ServerCodeMap.getCodeToVal(responseBody.getCode());
                    callback.onResult(validation);

                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            for (ErrorDetail error : errorResponse.getErrors()) {
                                Log.d(TAG, "Field: " + error.getField() + " Reason: " + error.getReason());
                            }
                            Validation validation = ServerCodeMap.getCodeToVal(errorResponse.getCode());
                            callback.onResult(validation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailAuthResponse> call, Throwable t) {

                /*요청과 응답에 실패했을 때*/
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });

    }

    public void checkEmailCode(String email, String verifyCode, com.example.moum.utils.Callback<Validation> callback) {

        EmailCodeRequest emailCodeRequest = new EmailCodeRequest(email, verifyCode);
        Call<EmailCodeResponse> result = signupApi.checkEmailCode(emailCodeRequest);
        result.enqueue(new Callback<EmailCodeResponse>() {
            @Override
            public void onResponse(Call<EmailCodeResponse> call, Response<EmailCodeResponse> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    EmailCodeResponse responseBody = response.body();
                    Log.d(TAG, "status: " + responseBody.getStatus() + "code: " + responseBody.getCode() + "message: " + responseBody.getMessage() + "data: " + responseBody.getData());
                    Validation validation = ServerCodeMap.getCodeToVal(responseBody.getCode());
                    callback.onResult(validation);
                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            for (ErrorDetail error : errorResponse.getErrors()) {
                                Log.d(TAG, "Field: " + error.getField() + " Reason: " + error.getReason());
                            }
                            Validation validation = ServerCodeMap.getCodeToVal(errorResponse.getCode());
                            callback.onResult(validation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailCodeResponse> call, Throwable t) {
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });
    }
}

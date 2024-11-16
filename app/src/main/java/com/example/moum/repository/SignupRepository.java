package com.example.moum.repository;

import android.util.Log;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.dto.EmailAuthRequest;
import com.example.moum.data.dto.EmailCodeRequest;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.ValueMap;
import com.example.moum.utils.Validation;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRepository {
    private static SignupRepository instance;
    private RetrofitClientManager retrofitClientManager;
    private SignupApi signupApi;
    private Retrofit retrofitClient;
    private String TAG = getClass().toString();

    private SignupRepository() {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getClient();
        signupApi = retrofitClient.create(SignupApi.class);
    }
    public SignupRepository(Retrofit retrofitClient, SignupApi signupApi){
        //TODO 테스트용
        this.retrofitClient = retrofitClient;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        this.signupApi = signupApi;
    }
    public static SignupRepository getInstance() {
        if (instance == null) {
            instance = new SignupRepository();
        }
        return instance;
    }

    public void emailAuth(String email, com.example.moum.utils.Callback<Result<Object>> callback) {

        EmailAuthRequest emailAuthRequest = new EmailAuthRequest(email);
        Call<SuccessResponse<String>> result = signupApi.emailAuth(emailAuthRequest);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Object> result = new Result<>(validation);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Object> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                /*요청과 응답에 실패했을 때*/
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });

    }

    public void checkEmailCode(String email, String verifyCode, com.example.moum.utils.Callback<Result<Object>> callback) {

        EmailCodeRequest emailCodeRequest = new EmailCodeRequest(email, verifyCode);
        Call<SuccessResponse<String>> result = signupApi.checkEmailCode(emailCodeRequest);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Object> result = new Result<>(validation);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Object> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void signup(SignupUser signupUser, com.example.moum.utils.Callback<Result<Object>> callback) {

        SignupRequest signupRequest = new SignupRequest(
            signupUser.getUsername(),
            signupUser.getPassword(),
            signupUser.getEmail(),
            signupUser.getName(),
            signupUser.getProfileDescription(),
            signupUser.getInstrument(),
            signupUser.getProficiency(),
            signupUser.getAddress(),
            signupUser.getEmailCode(),
            signupUser.getRecords()
        );

        MultipartBody.Part profileImage = null;
        if(signupUser.getProfileImage() != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), signupUser.getProfileImage());
            profileImage = MultipartBody.Part.createFormData("profileImage", signupUser.getProfileImage().getName(), requestFile);
        }

        Call<SuccessResponse<String>> result = signupApi.signup(signupRequest, profileImage);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Object> result = new Result<>(validation);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Object> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

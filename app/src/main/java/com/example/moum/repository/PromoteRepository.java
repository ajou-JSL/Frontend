package com.example.moum.repository;

import android.app.Application;
import android.util.Log;

import com.example.moum.data.api.PromoteApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PromoteRepository {
    private static PromoteRepository instance;
    private final PromoteApi promoteApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private PromoteRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        promoteApi = retrofitClient.create(PromoteApi.class);
    }

    public PromoteRepository(RetrofitClientManager retrofitClientManager, PromoteApi promoteApi) {
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.promoteApi = promoteApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static PromoteRepository getInstance(Application application) {
        if (instance == null) {
            instance = new PromoteRepository(application);
        }
        return instance;
    }

    public void createQr(Integer performId, com.example.moum.utils.Callback<Result<String>> callback) {
        Call<SuccessResponse<String>> result = promoteApi.createQr(performId);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
                    assert responseBody != null;
                    String qrUrl = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<String> result = new Result<>(validation, qrUrl);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<String> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<String> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadQr(Integer performId, com.example.moum.utils.Callback<Result<String>> callback) {
        Call<SuccessResponse<String>> result = promoteApi.loadQr(performId);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
                    assert responseBody != null;
                    String qrUrl = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<String> result = new Result<>(validation, qrUrl);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<String> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<String> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void deleteQr(Integer performId, com.example.moum.utils.Callback<Result<String>> callback) {
        Call<SuccessResponse<String>> result = promoteApi.deleteQr(performId);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
                    assert responseBody != null;
                    String qrUrl = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<String> result = new Result<>(validation, qrUrl);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<String> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<String> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }
}

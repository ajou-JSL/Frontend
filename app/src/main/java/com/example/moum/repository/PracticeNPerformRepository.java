package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.PracticeNPerformApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Content;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PracticeNPerformRepository {
    private static PracticeNPerformRepository instance;
    private final PracticeNPerformApi practiceNPerformApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private PracticeNPerformRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        practiceNPerformApi = retrofitClient.create(PracticeNPerformApi.class);
    }

    public PracticeNPerformRepository(RetrofitClientManager retrofitClientManager, PracticeNPerformApi practiceNPerformApi){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.practiceNPerformApi = practiceNPerformApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static PracticeNPerformRepository getInstance(Application application) {
        if (instance == null) {
            instance = new PracticeNPerformRepository(application);
        }
        return instance;
    }

    public void getPracticeroom(Integer practiceroomId, com.example.moum.utils.Callback<Result<Practiceroom>> callback){
        Call<SuccessResponse<Practiceroom>> result = practiceNPerformApi.getPracticeroom(practiceroomId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Practiceroom>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Practiceroom>> call, Response<SuccessResponse<Practiceroom>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Practiceroom> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Practiceroom practiceroom = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Practiceroom> result = new Result<>(validation, practiceroom);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Practiceroom> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Practiceroom>> call, Throwable t) {
                Result<Practiceroom> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void getPracticerooms(Integer page, Integer size, com.example.moum.utils.Callback<Result<List<Practiceroom>>> callback){
        Call<SuccessResponse<Content<List<Practiceroom>>>> result = practiceNPerformApi.getPracticerooms(page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Content<List<Practiceroom>>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Content<List<Practiceroom>>>> call, Response<SuccessResponse<Content<List<Practiceroom>>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Content<List<Practiceroom>>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Practiceroom> practicerooms = responseBody.getData().getContent();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Practiceroom>> result = new Result<>(validation, practicerooms);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Practiceroom>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Content<List<Practiceroom>>>> call, Throwable t) {
                Result<List<Practiceroom>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void getPerformHall(Integer performHallId, com.example.moum.utils.Callback<Result<PerformanceHall>> callback){
        Call<SuccessResponse<PerformanceHall>> result = practiceNPerformApi.getPerformHall(performHallId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<PerformanceHall>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<PerformanceHall>> call, Response<SuccessResponse<PerformanceHall>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<PerformanceHall> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    PerformanceHall performanceHall = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<PerformanceHall> result = new Result<>(validation, performanceHall);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<PerformanceHall> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<PerformanceHall>> call, Throwable t) {
                Result<PerformanceHall> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void getPerformHalls(Integer page, Integer size, com.example.moum.utils.Callback<Result<List<PerformanceHall>>> callback){
        Call<SuccessResponse<Content<List<PerformanceHall>>>> result = practiceNPerformApi.getPerformHalls(page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Content<List<PerformanceHall>>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Content<List<PerformanceHall>>>> call, Response<SuccessResponse<Content<List<PerformanceHall>>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Content<List<PerformanceHall>>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<PerformanceHall> performanceHalls = responseBody.getData().getContent();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<PerformanceHall>> result = new Result<>(validation, performanceHalls);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<PerformanceHall>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Content<List<PerformanceHall>>>> call, Throwable t) {
                Result<List<PerformanceHall>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }
}

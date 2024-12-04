package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ArticleApi;
import com.example.moum.data.api.PerformApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.MoumRequest;
import com.example.moum.data.dto.PerformRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Content;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerformRepository {
    private static PerformRepository instance;
    private final PerformApi performApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private PerformRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        performApi = retrofitClient.create(PerformApi.class);
    }

    public PerformRepository(RetrofitClientManager retrofitClientManager, PerformApi performApi){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.performApi = performApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static PerformRepository getInstance(Application application) {
        if (instance == null) {
            instance = new PerformRepository(application);
        }
        return instance;
    }

    public void createPerform(Performance perform, File file, com.example.moum.utils.Callback<Result<Performance>> callback){
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if(file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            profileImage = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }
        else{
            RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);
            profileImage = MultipartBody.Part.createFormData("file", null, emptyRequestBody);
        }
        PerformRequest performRequest = new PerformRequest(perform.getPerformanceName(), perform.getPerformanceDescription(), perform.getPerformanceLocation(), perform.getPerformanceStartDate(), perform.getPerformanceEndDate(), perform.getPerformancePrice(), perform.getMembersId(), perform.getTeamId(), perform.getMoumId(), perform.getMusics(), perform.getGenre());
        Call<SuccessResponse<Performance>> result = performApi.createPerform(profileImage, performRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Performance>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Performance>> call, Response<SuccessResponse<Performance>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Performance> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Performance performance = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Performance> result = new Result<>(validation, performance);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Performance> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Performance>> call, Throwable t) {
                Result<Performance> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void updatePerform(Integer performId, Performance perform, File file, com.example.moum.utils.Callback<Result<Performance>> callback){
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if(file != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            profileImage = MultipartBody.Part.createFormData("file", "temp_image.jpg", requestFile);
        }
        else{
            RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);
            profileImage = MultipartBody.Part.createFormData("file", null, emptyRequestBody);
        }
        PerformRequest performRequest = new PerformRequest(perform.getPerformanceName(), perform.getPerformanceDescription(), perform.getPerformanceLocation(), perform.getPerformanceStartDate(), perform.getPerformanceEndDate(), perform.getPerformancePrice(), perform.getMembersId(), null, perform.getMoumId(), perform.getMusics(), perform.getGenre());
        Call<SuccessResponse<Performance>> result = performApi.updatePerform(performId, profileImage, performRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Performance>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Performance>> call, Response<SuccessResponse<Performance>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Performance> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Performance performance = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Performance> result = new Result<>(validation, performance);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Performance> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Performance>> call, Throwable t) {
                Result<Performance> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void deletePerform(Integer performId, com.example.moum.utils.Callback<Result<Performance>> callback){
        Call<SuccessResponse<Performance>> result = performApi.deletePerform(performId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Performance>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Performance>> call, Response<SuccessResponse<Performance>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Performance> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Performance performance = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Performance> result = new Result<>(validation, performance);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Performance> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Performance>> call, Throwable t) {
                Result<Performance> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadPerform(Integer performId, com.example.moum.utils.Callback<Result<Performance>> callback){
        Call<SuccessResponse<Performance>> result = performApi.loadPerform(performId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Performance>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Performance>> call, Response<SuccessResponse<Performance>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Performance> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Performance performance = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Performance> result = new Result<>(validation, performance);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Performance> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Performance>> call, Throwable t) {
                Result<Performance> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadPerforms(Integer page, Integer size, com.example.moum.utils.Callback<Result<List<Performance>>> callback){
        Call<SuccessResponse<List<Performance>>> result = performApi.loadPerforms(page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Performance>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Performance>>> call, Response<SuccessResponse<List<Performance>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Performance>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Performance> articles = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Performance>> result = new Result<>(validation, articles);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Performance>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Performance>>> call, Throwable t) {
                Result<List<Performance>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadPerformsHot(Integer page, Integer size, com.example.moum.utils.Callback<Result<List<Performance>>> callback){
       Call<SuccessResponse<Content<List<Performance>>>> result = performApi.loadPerformsHot(page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Content<List<Performance>>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Content<List<Performance>>>> call, Response<SuccessResponse<Content<List<Performance>>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Content<List<Performance>>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Performance> articles = responseBody.getData().getContent();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Performance>> result = new Result<>(validation, articles);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Performance>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Content<List<Performance>>>> call, Throwable t) {
                Result<List<Performance>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadPerformOfMoum(Integer moumId, com.example.moum.utils.Callback<Result<Performance>> callback){
        Call<SuccessResponse<Performance>> result = performApi.loadPerformOfMoum(moumId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Performance>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Performance>> call, Response<SuccessResponse<Performance>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Performance> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Performance performance = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Performance> result = new Result<>(validation, performance);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Performance> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Performance>> call, Throwable t) {
                Result<Performance> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

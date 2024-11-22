package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.PerformApi;
import com.example.moum.data.api.PromoteApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.PerformRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
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

    public PromoteRepository(RetrofitClientManager retrofitClientManager, PromoteApi promoteApi){
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

}

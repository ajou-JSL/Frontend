package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.TeamApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Team;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.User;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TeamRepository {
    private static TeamRepository instance;
    private final TeamApi teamApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private TeamRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        teamApi = retrofitClient.create(TeamApi.class);
    }

    public TeamRepository(RetrofitClientManager retrofitClientManager, TeamApi teamApi){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.teamApi = teamApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static TeamRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TeamRepository(application);
        }
        return instance;
    }

    public void loadTeam(Integer teamId, com.example.moum.utils.Callback<Result<Team>> callback){
        Call<SuccessResponse<Team>> result = teamApi.loadTeam(teamId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Team>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Team>> call, Response<SuccessResponse<Team>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Team> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Team team = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Team> result = new Result<>(validation, team);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Team> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Team>> call, Throwable t) {
                Result<Team> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadTeams(com.example.moum.utils.Callback<Result<List<Team>>> callback){
        Call<SuccessResponse<List<Team>>> result = teamApi.loadTeams();
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Team>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Team>>> call, Response<SuccessResponse<List<Team>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Team>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Team> teams = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Team>> result = new Result<>(validation, teams);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Team>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Team>>> call, Throwable t) {
                Result<List<Team>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadTeamsAsLeader(String memberId, com.example.moum.utils.Callback<Result<List<Team>>> callback){
        Call<SuccessResponse<List<Team>>> result = teamApi.loadTeamsAsLeader(memberId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Team>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Team>>> call, Response<SuccessResponse<List<Team>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Team>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Team> teams = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Team>> result = new Result<>(validation, teams);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Team>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Team>>> call, Throwable t) {
                Result<List<Team>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

package com.example.moum.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.TeamApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Group;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.User;
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

    private TeamRepository() {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl("http:///223.130.162.175:8080/");
        retrofitClient = retrofitClientManager.getClient();//이후에는 AuthClient()로 교체 필요!
        teamApi = retrofitClient.create(TeamApi.class);
    }

    public TeamRepository(RetrofitClientManager retrofitClientManager, TeamApi teamApi){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.teamApi = teamApi;
        retrofitClientManager.setBaseUrl("http:///223.130.162.175:8080/");
    }

    public static TeamRepository getInstance() {
        if (instance == null) {
            instance = new TeamRepository();
        }
        return instance;
    }

    public void loadGroups(String memberId, com.example.moum.utils.Callback<Result<List<Group>>> callback){
        Call<SuccessResponse<List<Group>>> result = teamApi.loadGroups(memberId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Group>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Group>>> call, Response<SuccessResponse<List<Group>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Group>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Group> groups = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Group>> result = new Result<>(validation, groups);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Group>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Group>>> call, Throwable t) {
                Result<List<Group>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadMembersOfGroup(Integer groupId, com.example.moum.utils.Callback<Result<List<User>>> callback){
        Call<SuccessResponse<List<User>>> result = teamApi.loadMembersOfGroup(groupId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<User>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<User>>> call, Response<SuccessResponse<List<User>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<User>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<User> members = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<User>> result = new Result<>(validation, members);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<User>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<User>>> call, Throwable t) {
                Result<List<User>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

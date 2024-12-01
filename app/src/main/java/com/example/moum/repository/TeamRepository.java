package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.TeamApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.dto.TeamRequest;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;
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

    public void createTeam(Team team, File teamProfile, com.example.moum.utils.Callback<Result<Team>> callback){
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if(teamProfile != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), teamProfile);
            profileImage = MultipartBody.Part.createFormData("file", teamProfile.getName(), requestFile);
        }
        else{
            RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);
            profileImage = MultipartBody.Part.createFormData("file", null, emptyRequestBody);
        }
        TeamRequest teamRequest = new TeamRequest(team.getLeaderId(), team.getTeamName(), team.getDescription(), team.getGenre(), team.getLocation(), team.getRecords(), team.getVideoUrl());
        Call<SuccessResponse<Team>> result = teamApi.createTeam(profileImage, teamRequest);

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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

    public void updateTeam(Integer teamId, Team team, File teamProfile, com.example.moum.utils.Callback<Result<Team>> callback){
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if(teamProfile != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), teamProfile);
            profileImage = MultipartBody.Part.createFormData("file", teamProfile.getName(), requestFile);
        }
        else{
            RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);
            profileImage = MultipartBody.Part.createFormData("file", null, emptyRequestBody);
        }
        TeamRequest teamRequest = new TeamRequest(null, team.getTeamName(), team.getDescription(), team.getGenre(), team.getLocation(), team.getRecords(), team.getVideoUrl());
        Call<SuccessResponse<Team>> result = teamApi.updateTeam(teamId, profileImage, teamRequest);

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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

    public void deleteTeam(Integer teamId, com.example.moum.utils.Callback<Result<Team>> callback){
        Call<SuccessResponse<Team>> result = teamApi.deleteTeam(teamId);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

    public void loadTeamsAsMember(Integer memberId, com.example.moum.utils.Callback<Result<List<Team>>> callback){
        Call<SuccessResponse<List<Team>>> result = teamApi.loadTeamsAsMember(memberId);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

    public void kickMemberFromTeam(Integer teamId, Integer memberId, com.example.moum.utils.Callback<Result<Team>> callback){
        Call<SuccessResponse<Team>> result = teamApi.kickMemberFromTeam(teamId, memberId);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

    public void leaveTeam(Integer teamId, com.example.moum.utils.Callback<Result<Team>> callback){
        Call<SuccessResponse<Team>> result = teamApi.leaveTeam(teamId);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

    public void inviteMemberToTeam(Integer teamId, Integer memberId, com.example.moum.utils.Callback<Result<Member>> callback){
        Call<SuccessResponse<Member>> result = teamApi.inviteMemberToTeam(teamId, memberId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Member>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Member>> call, Response<SuccessResponse<Member>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Member> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Member member = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Member> result = new Result<>(validation, member);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Member> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Member>> call, Throwable t) {
                Result<Member> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadTeamToTeamName(String teamName, com.example.moum.utils.Callback<Result<Team>> callback){
        Call<SuccessResponse<Team>> result = teamApi.loadTeamToName(teamName);
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
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
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

}

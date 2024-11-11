package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ProfileApi;
import com.example.moum.data.api.TeamApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.MemberProfileUpdateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileRepository {
    private static ProfileRepository instance;
    private final ProfileApi profileApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private ProfileRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        profileApi = retrofitClient.create(ProfileApi.class);
    }

    public ProfileRepository(RetrofitClientManager retrofitClientManager, ProfileApi profileApi){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.profileApi = profileApi;
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
    }

    public static ProfileRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ProfileRepository(application);
        }
        return instance;
    }

    public void loadMemberProfile(Integer targetMemberId, com.example.moum.utils.Callback<Result<Member>> callback){
        Call<SuccessResponse<Member>> result = profileApi.loadMemberProfile(targetMemberId);
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

    public void updateMemberProfile(Integer memberId, Member updatedMember, com.example.moum.utils.Callback<Result<Member>> callback){
        MemberProfileUpdateRequest request = new MemberProfileUpdateRequest(updatedMember.getName(), updatedMember.getUsername(), updatedMember.getProfileDescription(), updatedMember.getEmail(), updatedMember.getProfileImageUrl(), updatedMember.getProficiency(), updatedMember.getInstrument(), updatedMember.getAddress());
        Call<SuccessResponse<Member>> result = profileApi.updateMemberProfile(memberId, request);
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


}
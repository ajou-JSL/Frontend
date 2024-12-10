package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ProfileApi;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.MemberProfileRankResponse;
import com.example.moum.data.dto.MemberProfileUpdateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Content;
import com.example.moum.data.entity.Member;
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

public class ProfileRepository {
    private static ProfileRepository instance;
    private final ProfileApi profileApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ProfileRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        profileApi = retrofitClient.create(ProfileApi.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ProfileRepository(RetrofitClientManager retrofitClientManager) {
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getAuthClient(null);
        profileApi = retrofitClient.create(ProfileApi.class);
        ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ProfileRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ProfileRepository(application);
        }
        return instance;
    }

    public void loadMemberProfile(Integer targetMemberId, com.example.moum.utils.Callback<Result<Member>> callback) {
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
                } else {
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

    public void updateMemberProfile(Integer memberId, File file, Member updatedMember, com.example.moum.utils.Callback<Result<Member>> callback) {
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            profileImage = MultipartBody.Part.createFormData("file", "temp_image.jpg", requestFile);
        } else {
            RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);
            profileImage = MultipartBody.Part.createFormData("file", null, emptyRequestBody);
        }
        MemberProfileUpdateRequest request = new MemberProfileUpdateRequest(updatedMember.getName(), updatedMember.getUsername(),
                updatedMember.getProfileDescription(), updatedMember.getEmail(), updatedMember.getProficiency(), updatedMember.getInstrument(),
                updatedMember.getAddress(), updatedMember.getMemberRecords(), updatedMember.getGenres(), updatedMember.getVideoUrl());
        Call<SuccessResponse<Member>> result = profileApi.updateMemberProfile(memberId, profileImage, request);
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
                } else {
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

    public void loadMembersByRank(Integer page, Integer size, com.example.moum.utils.Callback<Result<List<MemberProfileRankResponse>>> callback) {
        Call<SuccessResponse<Content<List<MemberProfileRankResponse>>>> result = profileApi.loadMembersByRank(page, size);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Content<List<MemberProfileRankResponse>>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Content<List<MemberProfileRankResponse>>>> call,
                    Response<SuccessResponse<Content<List<MemberProfileRankResponse>>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Content<List<MemberProfileRankResponse>>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<MemberProfileRankResponse> members = responseBody.getData().getContent();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<MemberProfileRankResponse>> result = new Result<>(validation, members);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<MemberProfileRankResponse>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Content<List<MemberProfileRankResponse>>>> call, Throwable t) {
                Result<List<MemberProfileRankResponse>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }
}

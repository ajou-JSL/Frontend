package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ChatroomApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ChatroomCreateRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Team;
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

public class ChatroomRepository {
    private static ChatroomRepository instance;
    private final ChatroomApi chatroomApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();

    private ChatroomRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        chatroomApi = retrofitClient.create(ChatroomApi.class);
    }

//    public ChatRepository(RetrofitClientManager retrofitClientManager, SseClientManager sseClientManager, ChatApi chatApi){
//        this.retrofitClientManager = retrofitClientManager;
//        this.sseClientManager = sseClientManager;
//        this.retrofitClient = retrofitClientManager.getClient();
//        this.sseClient = sseClientManager.getClient();
//        this.chatApi = chatApi;
//        retrofitClientManager.setBaseUrl("http://172.21.38.228:8070/");
//    }

    public static ChatroomRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ChatroomRepository(application);
        }
        return instance;
    }

    public void loadChatrooms(Integer memberId, com.example.moum.utils.Callback<Result<List<Chatroom>>> callback){
        Call<SuccessResponse<List<Chatroom>>> result = chatroomApi.loadChatrooms(memberId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Chatroom>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Chatroom>>> call, Response<SuccessResponse<List<Chatroom>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Chatroom>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Chatroom> chatrooms = responseBody.getData();

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Chatroom>> result = new Result<>(validation, chatrooms);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Chatroom>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Chatroom>>> call, Throwable t) {
                Result<List<Chatroom>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void createChatroom(Chatroom chatroom, File chatroomProfileFile, ArrayList<Integer> participants, com.example.moum.utils.Callback<Result<Chatroom>> callback){
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if(chatroomProfileFile != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), chatroomProfileFile);
            profileImage = MultipartBody.Part.createFormData("chatroomProfile", chatroomProfileFile.getName(), requestFile);
        }
        ChatroomCreateRequest request = new ChatroomCreateRequest(chatroom.getName(), chatroom.getType().getValue(), chatroom.getTeamId(), chatroom.getLeaderId(), participants);

        /*client 요청 보냄*/
        Call<SuccessResponse<Chatroom>> result = chatroomApi.createChatroom(profileImage, request);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Chatroom>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Chatroom>> call, Response<SuccessResponse<Chatroom>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Chatroom> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Chatroom createdChatroom = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Chatroom> result = new Result<>(validation, createdChatroom);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Chatroom> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Chatroom>> call, Throwable t) {
                Result<Chatroom> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadMembersOfChatroom(Integer chatroomId, com.example.moum.utils.Callback<Result<List<Member>>> callback){
        Call<SuccessResponse<List<Member>>> result = chatroomApi.loadMembersOfChatroom(chatroomId);
        result.enqueue(new retrofit2.Callback<SuccessResponse<List<Member>>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<List<Member>>> call, Response<SuccessResponse<List<Member>>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<List<Member>> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    List<Member> members = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<List<Member>> result = new Result<>(validation, members);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<List<Member>> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<List<Member>>> call, Throwable t) {
                Result<List<Member>> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

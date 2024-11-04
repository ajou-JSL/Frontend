package com.example.moum.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ChatApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatStreamResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Group;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.User;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.repository.client.SseClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatRepository {
    private static ChatRepository instance;
    private final ChatApi chatApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final SseClientManager sseClientManager;
    private final OkHttpClient sseClient;
    private final String TAG = getClass().toString();

    private ChatRepository() {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl("http://172.21.38.228:8070/");
        retrofitClient = retrofitClientManager.getClient();//이후에는 AuthClient()로 교체 필요!
        chatApi = retrofitClient.create(ChatApi.class);
        sseClientManager = new SseClientManager();
        sseClient = sseClientManager.getClient();
    }

    public ChatRepository(RetrofitClientManager retrofitClientManager, SseClientManager sseClientManager, ChatApi chatApi){
        this.retrofitClientManager = retrofitClientManager;
        this.sseClientManager = sseClientManager;
        this.retrofitClient = retrofitClientManager.getClient();
        this.sseClient = sseClientManager.getClient();
        this.chatApi = chatApi;
        retrofitClientManager.setBaseUrl("http://172.21.38.228:8070/");
    }

    public static ChatRepository getInstance() {
        if (instance == null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public void chatSend(Chat chat, com.example.moum.utils.Callback<Result<Chat>> callback) {

        ChatSendRequest chatSendRequest = new ChatSendRequest(chat.getSender(), chat.getReceiver(), chat.getMessage());
        Call<SuccessResponse<Chat>> result = chatApi.chatSend(chat.getChatroomId(), chatSendRequest);
        result.enqueue(new retrofit2.Callback<SuccessResponse<Chat>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<Chat>> call, Response<SuccessResponse<Chat>> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Chat> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());
                    Chat sentChat = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Chat> result = new Result<Chat>(validation, sentChat);
                    callback.onResult(result);

                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Chat> result = new Result<Chat>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Chat>> call, Throwable t) {
                Result<Chat> result = new Result<Chat>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void receiveRecentChat(Integer chatroomId, com.example.moum.utils.Callback<Result<Chat>> callback) {
        /*URL 생성*/
        HttpUrl url = HttpUrl.parse(sseClientManager.getBaseUrl())
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("chat")
                .addPathSegment("test")
                .addPathSegment(String.valueOf(chatroomId))
                .build();

        /*GET 요청 생성*/
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "text/event-stream")
                .build();

        EventSource eventSource = EventSources.createFactory(sseClient).newEventSource(request, new EventSourceListener() {
            @Override
            public void onClosed(@NonNull okhttp3.sse.EventSource eventSource) {
                super.onClosed(eventSource);
                Log.e(TAG, "onClosed");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@NonNull okhttp3.sse.EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
                super.onEvent(eventSource, id, type, data);
                Log.e(TAG, "onEvent");
                ChatStreamResponse receivedChat = new Gson().fromJson(data, ChatStreamResponse.class);
                Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
                Chat chat = new Chat(receivedChat.getSender(), receivedChat.getReceiver(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
                Result<Chat> result = new Result<Chat>(validation, chat);
                callback.onResult(result);
            }

            @Override
            public void onFailure(@NonNull okhttp3.sse.EventSource eventSource, @Nullable Throwable t, @Nullable okhttp3.Response response) {
                super.onFailure(eventSource, t, response);
                Log.e(TAG, "onFailure");
                Result<Chat> result = new Result<Chat>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }

            @Override
            public void onOpen(@NonNull okhttp3.sse.EventSource eventSource, @NonNull okhttp3.Response response) {
                Log.e(TAG, "onOpen");
                super.onOpen(eventSource, response);
            }
        });


//        sseClient.newCall(request).enqueue(new okhttp3.Callback() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
//                Log.e(TAG, "response 도착");
//                if(response.isSuccessful()) {
//
//                    /*성공적으로 응답을 받았을 때*/
//                    Log.e(TAG, "SSE  응답 성공");
//                    try (BufferedReader reader = new BufferedReader(
//                            new InputStreamReader(response.body().byteStream()))) {
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            ChatStreamResponse receivedChat = new Gson().fromJson(line, ChatStreamResponse.class);
//                            if (receivedChat != null) {
//                                Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
//                                Chat chat = new Chat(receivedChat.getSender(), receivedChat.getReceiver(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
//                                Result<Chat> result = new Result<Chat>(validation, chat);
//                                callback.onResult(result);
//                            }
//                        }
//                    }
//                }
//                else{
//
//                    /*응답은 받았으나 문제 발생 시*/
//                    try {
//                        ChatErrorResponse errorResponse = new Gson().fromJson(response.body().string(), ChatErrorResponse.class);
//                        if (errorResponse != null) {
//                            Log.e(TAG, "Status: " + errorResponse.getStatus() + "Code: " + errorResponse.getCode() + "Messsage: " + errorResponse.getMessage());
//                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
//                            Result<Chat> result = new Result<Chat>(validation);
//                            callback.onResult(result);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//                Result<Chat> result = new Result<Chat>(Validation.NETWORK_FAILED);
//                callback.onResult(result);
//            }
//        });
    }

    public void receiveOldChat(Integer chatroomId, LocalDateTime beforeTimestamp, com.example.moum.utils.Callback<Result<Chat>> callback) {
        /*URL 생성*/
        HttpUrl url = HttpUrl.parse(sseClientManager.getBaseUrl())
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("chat")
                .addPathSegment("test")
                .addPathSegment(String.valueOf(chatroomId))
                .addPathSegment("older")
                .addQueryParameter("beforeTimestamp", beforeTimestamp.toString())
                .build();

        /*GET 요청 생성*/
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        /*SSE client를 통해 요청 전송*/
        sseClient.newCall(request).enqueue(new okhttp3.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if(response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(response.body().byteStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            ChatStreamResponse receivedChat = new Gson().fromJson(line, ChatStreamResponse.class);
                            if (receivedChat != null) {
                                Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
                                Chat chat = new Chat(receivedChat.getSender(), receivedChat.getReceiver(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
                                Result<Chat> result = new Result<Chat>(validation, chat);
                                callback.onResult(result);
                            }
                        }
                    }
                }
                else{
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.body().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Chat> result = new Result<Chat>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Result<Chat> result = new Result<Chat>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void loadChatrooms(String memberId, com.example.moum.utils.Callback<Result<List<Chatroom>>> callback){
        Call<SuccessResponse<List<Chatroom>>> result = chatApi.loadChatrooms(memberId);
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

    public void createChatroom(String chatroomName, File chatroomProfileFile, ArrayList<User> participants, com.example.moum.utils.Callback<Result<List<Chatroom>>> callback){
        Call<SuccessResponse<List<Chatroom>>> result = chatApi.loadChatrooms(memberId);
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

}

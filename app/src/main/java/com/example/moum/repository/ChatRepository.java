package com.example.moum.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.moum.data.api.ChatApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatSendResponse;
import com.example.moum.data.dto.ChatStreamResponse;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Result;
import com.example.moum.utils.Callback;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    }

    public static ChatRepository getInstance() {
        if (instance == null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public void chatSend(Chat chat, com.example.moum.utils.Callback<Result<Chat>> callback) {

        ChatSendRequest chatSendRequest = new ChatSendRequest(chat.getSender(), chat.getReceiver(), chat.getMessage());
        Call<ChatSendResponse> result = chatApi.chatSend(chat.getChatroomId(), chatSendRequest);
        result.enqueue(new retrofit2.Callback<ChatSendResponse>() {
            @Override
            public void onResponse(Call<ChatSendResponse> call, Response<ChatSendResponse> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    ChatSendResponse responseBody = response.body();
                    Log.e(TAG, "Status: " + responseBody.getStatus() + " Code: " + responseBody.getCode() + " Message: " + responseBody.getMessage());
                    ChatSendResponse.SentChat sentChat = responseBody.getData();
                    Log.e(TAG, "[Data] sender: " + sentChat.getSender() + " Receiver: " + sentChat.getReceiver() + " Message: " + sentChat.getMessage() + " Timestamp: " + sentChat.getTimestamp() + " ChatroomId: " + sentChat.getChatroomId());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());

                    Chat chat = new Chat(sentChat.getSender(), sentChat.getReceiver(), sentChat.getMessage(), sentChat.getChatroomId(), sentChat.getTimestamp());
                    Result<Chat> result = new Result<Chat>(validation, chat);
                    callback.onResult(result);

                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, "Status: " + errorResponse.getStatus() + "Code: " + errorResponse.getCode() + "Messsage: " + errorResponse.getMessage());
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
            public void onFailure(Call<ChatSendResponse> call, Throwable t) {
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
                .build();

        /*SSE client를 통해 요청 전송*/
        sseClient.newCall(request).enqueue(new okhttp3.Callback() {
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
                            Log.e(TAG, "Status: " + errorResponse.getStatus() + "Code: " + errorResponse.getCode() + "Messsage: " + errorResponse.getMessage());
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
                            Log.e(TAG, "Status: " + errorResponse.getStatus() + "Code: " + errorResponse.getCode() + "Messsage: " + errorResponse.getMessage());
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
}

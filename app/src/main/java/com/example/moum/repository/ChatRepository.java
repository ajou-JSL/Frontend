package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.R;
import com.example.moum.data.api.ChatApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatStreamResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

public class ChatRepository {
    private static ChatRepository instance;
    private final ChatApi chatApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final String TAG = getClass().toString();
    private final SharedPreferenceManager sharedPreferenceManager;
    private final String accessToken;

    private ChatRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.CHAT_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getAuthClient(application);
        chatApi = retrofitClient.create(ChatApi.class);

        this.sharedPreferenceManager = new SharedPreferenceManager(application, application.getString(R.string.preference_file_key));
        this.accessToken = sharedPreferenceManager.getCache(application.getString(R.string.user_access_token_key), "no-access-token");
    }

    public ChatRepository(Application application, RetrofitClientManager retrofitClientManager){
        this.retrofitClientManager = retrofitClientManager;
        this.retrofitClient = retrofitClientManager.getAuthClient(application);
        this.chatApi = retrofitClient.create(ChatApi.class);

        this.sharedPreferenceManager = new SharedPreferenceManager(application, application.getString(R.string.preference_file_key));
        this.accessToken = sharedPreferenceManager.getCache(application.getString(R.string.user_access_token_key), "no-access-token");
    }

    public static ChatRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ChatRepository(application);
        }
        return instance;
    }

    public void chatSend(Chat chat, com.example.moum.utils.Callback<Result<Chat>> callback) {
        ChatSendRequest chatSendRequest = new ChatSendRequest(chat.getMessage());
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
        HttpUrl url = HttpUrl.parse(BaseUrl.CHAT_SERVER_PATH.getUrl())
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("chat")
                .addPathSegment(String.valueOf(chatroomId))
                .build();

        /*GET 요청 생성*/
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "text/event-stream")
                .addHeader("access", accessToken)
                .build();

        /*OkSse로 SSE 통신 스트림 연결*/
        OkSse okSse = new OkSse();
        Handler handler = new Handler(Looper.getMainLooper());
        ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.e(TAG, "SSE connection open - recent");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.e(TAG, "Event: "+event+ " => Message; "+message + " - recent");
                if(event.equals("message")){
                    ChatStreamResponse receivedChat = new Gson().fromJson(message, ChatStreamResponse.class);
                    if (receivedChat != null) {
                        Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
                        Chat chat = new Chat(receivedChat.getSender(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
                        Result<Chat> result = new Result<Chat>(validation, chat);
                        /*viewModel에 도착하는 순서를 보장하기 위해 handler 사용*/
                        handler.post(() -> {
                            callback.onResult(result);
                        });

                    }
                }
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                Log.e(TAG, comment + " - recent");
            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                Log.e(TAG, "onRetryTime - recent");
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
                Log.e(TAG, "onRetryError - recent");
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {
                Log.e(TAG, "onClosed - recent");
                Validation validation = Validation.CHAT_RECEIVE_FAIL;
                Result<Chat> result = new Result<Chat>(validation);
                handler.post(() -> {
                    callback.onResult(result);
                });
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                Log.e(TAG, "onPreRetry - recent");
                return null;
            }
        });
    }

    public void receiveOldChat(Integer chatroomId, LocalDateTime beforeTimestamp, com.example.moum.utils.Callback<Result<Chat>> callback) {
        /*URL 생성*/
        HttpUrl url = HttpUrl.parse(BaseUrl.CHAT_SERVER_PATH.getUrl())
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("chat")
                .addPathSegment(String.valueOf(chatroomId))
                .addPathSegment("before-timestamp")
                .addQueryParameter("beforeTimestamp", beforeTimestamp.toString())
                .build();

        /*GET 요청 생성*/
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("access", accessToken)
                .build();

        /*OkSse로 SSE 통신 스트림 연결*/
        OkSse okSse = new OkSse();
        Handler handler = new Handler(Looper.getMainLooper());
        ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.e(TAG, "SSE connection open - old");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.e(TAG, "Event: "+event+ " => Message; "+message + "- old");
                if(event.equals("message")){
                    ChatStreamResponse receivedChat = new Gson().fromJson(message, ChatStreamResponse.class);
                    if (receivedChat != null) {
                        Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
                        Chat chat = new Chat(receivedChat.getSender(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
                        Result<Chat> result = new Result<Chat>(validation, chat);
                        /*viewModel에 도착하는 순서를 보장하기 위해 handler 사용*/
                        handler.post(() -> {
                            callback.onResult(result);
                        });

                    }
                }
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                Log.e(TAG, comment + "- old");
            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                Log.e(TAG, "onRetryTime - old");
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
                Log.e(TAG, "onRetryError - old");
                Log.e(TAG, response.toString());
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {
                Log.e(TAG, "onClosed - old");
                Validation validation = Validation.CHAT_RECEIVE_FAIL;
                Result<Chat> result = new Result<Chat>(validation);
                handler.post(() -> {
                    callback.onResult(result);
                });
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                Log.e(TAG, "onPreRetry - old");
                return null;
            }
        });
    }
}

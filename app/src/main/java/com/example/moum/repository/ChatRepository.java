package com.example.moum.repository;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.moum.data.api.ChatApi;
import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ChatSendRequest;
import com.example.moum.data.dto.ChatStreamResponse;
import com.example.moum.data.dto.ChatroomCreateRequest;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatRepository {
    private static ChatRepository instance;
    private final ChatApi chatApi;
    private final RetrofitClientManager retrofitClientManager;
    private final Retrofit retrofitClient;
    private final SseClientManager sseClientManager;
    private final OkHttpClient sseClient;
    private final String TAG = getClass().toString();
    private static String CHAT_BASE_URL = "http://172.21.89.157:8070/";

    private ChatRepository(Application application) {

        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(CHAT_BASE_URL);
        retrofitClient = retrofitClientManager.getClient();
        chatApi = retrofitClient.create(ChatApi.class);
        sseClientManager = new SseClientManager();
        sseClientManager.setBaseUrl(CHAT_BASE_URL);
        sseClient = sseClientManager.getClient();
    }

//    public ChatRepository(RetrofitClientManager retrofitClientManager, SseClientManager sseClientManager, ChatApi chatApi){
//        this.retrofitClientManager = retrofitClientManager;
//        this.sseClientManager = sseClientManager;
//        this.retrofitClient = retrofitClientManager.getClient();
//        this.sseClient = sseClientManager.getClient();
//        this.chatApi = chatApi;
//        retrofitClientManager.setBaseUrl("http://172.21.38.228:8070/");
//    }

    public static ChatRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ChatRepository(application);
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
                .addPathSegment("paging")
                .addPathSegment(String.valueOf(chatroomId))
                .build();

        /*GET 요청 생성*/
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "text/event-stream")
                .build();

        /*OkSse로 SSE 통신 스트림 연결*/
        OkSse okSse = new OkSse();
        Handler handler = new Handler(Looper.getMainLooper());
        ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.d(TAG, "SSE connection open - recent");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.d(TAG, "Event: "+event+ " => Message; "+message + " - recent");
                if(event.equals("message")){
                    ChatStreamResponse receivedChat = new Gson().fromJson(message, ChatStreamResponse.class);
                    if (receivedChat != null) {
                        Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
                        Chat chat = new Chat(receivedChat.getSender(), receivedChat.getReceiver(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
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
                Log.d(TAG, comment + " - recent");
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

        /*OkSse로 SSE 통신 스트림 연결*/
        OkSse okSse = new OkSse();
        Handler handler = new Handler(Looper.getMainLooper());
        ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.d(TAG, "SSE connection open - old");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.d(TAG, "Event: "+event+ " => Message; "+message + "- old");
                if(event.equals("message")){
                    ChatStreamResponse receivedChat = new Gson().fromJson(message, ChatStreamResponse.class);
                    if (receivedChat != null) {
                        Validation validation = Validation.CHAT_RECEIVE_SUCCESS;
                        Chat chat = new Chat(receivedChat.getSender(), receivedChat.getReceiver(), receivedChat.getMessage(), receivedChat.getChatroomId(), receivedChat.getTimestamp());
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
                Log.d(TAG, comment + "- old");
            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                Log.e(TAG, "onRetryTime - old");
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
                Log.e(TAG, "onRetryError - old");
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {
                Log.e(TAG, "onClosed - old");
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                Log.e(TAG, "onPreRetry - old");
                return null;
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

    public void createChatroom(Integer groupId, String chatroomName, File chatroomProfileFile, ArrayList<User> participants, com.example.moum.utils.Callback<Result<String>> callback){
        /*processing into DTO*/
        MultipartBody.Part profileImage = null;
        if(chatroomProfileFile != null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), chatroomProfileFile);
            profileImage = MultipartBody.Part.createFormData("profileImage", chatroomProfileFile.getName(), requestFile);
        }
        ArrayList<ChatroomCreateRequest.Member> members = new ArrayList<>();
        for(User user : participants){
            ChatroomCreateRequest.Member member = new ChatroomCreateRequest.Member(user.getName());
            members.add(member);
        }
        ChatroomCreateRequest request = new ChatroomCreateRequest(groupId, chatroomName, members);

        Call<SuccessResponse<String>> result = chatApi.createChatroom(profileImage, request);
        result.enqueue(new retrofit2.Callback<SuccessResponse<String>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
                    Log.e(TAG, responseBody.toString());

                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<String> result = new Result<>(validation);
                    callback.onResult(result);
                }
                else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ChatErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ChatErrorResponse.class);
                        if (errorResponse != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<String> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<String> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

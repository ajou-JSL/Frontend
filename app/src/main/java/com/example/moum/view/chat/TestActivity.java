package com.example.moum.view.chat;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moum.data.dto.ChatErrorResponse;
import com.example.moum.data.dto.ChatStreamResponse;
import com.example.moum.data.entity.Chat;
import com.example.moum.data.entity.Result;
import com.example.moum.databinding.ActivityTestBinding;
import com.example.moum.repository.client.SseClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

public class TestActivity extends AppCompatActivity {
    private String TAG = getClass().toString();
    ActivityTestBinding binding;
    private String CHAT_BASE_URL = "http://172.30.1.78/8070/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        TextView mTextview = binding.textviewTest;
        View view = binding.getRoot();
        setContentView(view);

        SseClientManager sseClientManager = new SseClientManager();
        sseClientManager.setBaseUrl(CHAT_BASE_URL);
        OkHttpClient sseClient = sseClientManager.getClient();
        ArrayList<String> messages = new ArrayList<>();

        String m;
        /*URL 생성*/
        HttpUrl url = HttpUrl.parse("http://172.30.1.78:8070/")
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("chat")
                .addPathSegment("test")
                .addPathSegment("paging")
                .addPathSegment(String.valueOf(0))
//                .addPathSegment("string")
                .build();

        /*GET 요청 생성*/
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "text/event-stream")
                .build();


        OkSse okSse = new OkSse();
        ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {

            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.e(TAG, "conexion open");
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.d(TAG, "Event: "+event+ " => Message; "+message);
                if(event.equals("message")){
                    try {
                        ChatStreamResponse receivedChat = new Gson().fromJson(message, ChatStreamResponse.class);
                        Log.e(TAG, receivedChat.getMessage());
                        messages.add(receivedChat.getMessage());
                        //Chat chat = new Chat(receivedChat.getSender(), receivedChat.getReceiver(), receivedChat.getMessage(), receivedChat.getChatroomId());
//                        JSONObject jsonObject = new JSONObject(message);
//                        final String mMessage = jsonObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "runOnUiThread");
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> mTextview.setText(receivedChat.getMessage()));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                Log.d(TAG, comment);
            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                Log.e(TAG, "onRetryTime");
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
                Log.e(TAG, "onRetryError");
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {
                Log.e(TAG, "onClosed");
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                Log.e(TAG, "onPreRetry");
                return null;
            }
        });

        /*버튼 이벤트*/
        binding.buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageAll = "";
                for(String message : messages){
                    messageAll = messageAll.concat(message);
                }
                binding.textviewTest.setText(messageAll);
            }
        });
    }
}

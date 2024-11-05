package com.example.moum.repository.client;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SseClientManager {

    private OkHttpClient sseClient = null;
    private OkHttpClient authSseClient = null;
    private String BASE_URL = "";

    public void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    /*인증 없는 클라이언트를 사용할 때*/
    public OkHttpClient getClient() {
        if (sseClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            sseClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(true)
                    .connectionPool(new ConnectionPool(1, 1, TimeUnit.MINUTES))
                    .build();
        }
        return sseClient;
    }

    /*인증 있는 클라이언트를 사용할 때*/
    public OkHttpClient getAuthClient(Context context) {
        if (authSseClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            authSseClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new AuthInterceptor(context)) //이 요청에는 특별히 Auth가 이루어지도록 붙임
                    .retryOnConnectionFailure(true)
                    .connectionPool(new ConnectionPool(1, 1, TimeUnit.MINUTES))
                    .build();
        }
        return authSseClient;
    }
}
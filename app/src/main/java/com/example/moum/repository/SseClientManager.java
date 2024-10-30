package com.example.moum.repository;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SseClientManager {

    private OkHttpClient sseClient = null;
    private OkHttpClient authSseClient = null;
    private static String BASE_URL = "http://223.130.162.175:8080/";

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
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5,TimeUnit.MINUTES)
                    .writeTimeout(5,TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build();
        }
        return sseClient;
    }
}

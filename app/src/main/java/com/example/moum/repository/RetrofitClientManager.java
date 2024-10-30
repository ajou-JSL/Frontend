package com.example.moum.repository;

import android.content.Context;


import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientManager {
    private Retrofit retrofit = null;
    private Retrofit authRetrofit = null;
    private static String BASE_URL = "http://223.130.162.175:8080/";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    /*인증 없는 클라이언트를 사용할 때*/
    public Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5,TimeUnit.MINUTES)
                    .writeTimeout(5,TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /*인증된 클라이언트를 사용할 떄*/
    public Retrofit getAuthClient(Context context, String accessToken, String refreshToken) {

        if (authRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10,TimeUnit.MINUTES)
                    .writeTimeout(10,TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new AuthInterceptor(context, accessToken, refreshToken)) //이 요청에는 특별히 Auth가 이루어지도록 붙임
                    .build();
            authRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return authRetrofit;

    }
}
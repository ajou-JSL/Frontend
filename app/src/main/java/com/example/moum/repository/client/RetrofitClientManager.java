package com.example.moum.repository.client;

import android.content.Context;
import android.os.Build;


import androidx.annotation.RequiresApi;

import com.example.moum.data.entity.Genre;
import com.example.moum.utils.GenreTypeAdapter;
import com.example.moum.utils.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientManager {
    private Retrofit retrofit = null;
    private Retrofit authRetrofit = null;
    private String BASE_URL = "";

    public void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    /*인증 없는 클라이언트를 사용할 때*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Genre.class, new GenreTypeAdapter()) // Genre 어댑터 등록
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()) // LocalDateTime 어댑터 등록
                    .setPrettyPrinting().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    /*인증 있는 클라이언트를 사용할 때*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Retrofit getAuthClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthInterceptor(context)) //이 요청에는 특별히 Auth가 이루어지도록 붙임
                .build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Genre.class, new GenreTypeAdapter()) // Genre 어댑터 등록
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()) // LocalDateTime 어댑터 등록
                .setPrettyPrinting().create();
        authRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return authRetrofit;
    }
}

package com.example.moum.repository;

import android.app.Application;

import com.example.moum.repository.client.RetrofitClientManager;

import retrofit2.Retrofit;

public class UserboardRepository {
    private static UserboardRepository instance;
    private RetrofitClientManager retrofitClientManager;
    private Retrofit retrofitClient;
    private Retrofit authRetrofitClient;
    private String TAG = getClass().toString();


    public static UserboardRepository getInstance(Application application) {
        if (instance == null) {
  //          instance = new UserboardRepository(application);
        }
        return instance;

    }
}

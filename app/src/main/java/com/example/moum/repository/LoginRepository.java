package com.example.moum.repository;

import android.app.Application;
import android.health.connect.datatypes.AppInfo;
import android.util.Log;

import com.example.moum.data.api.LoginApi;
import com.example.moum.data.dto.ErrorDetail;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Token;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginRepository {
    private static LoginRepository instance;
    private RetrofitClientManager retrofitClientManager;
    private LoginApi loginApi;
    private LoginApi authLoginApi;
    private Retrofit retrofitClient;
    private Retrofit authRetrofitClient;
    private String TAG = getClass().toString();

    private LoginRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getClient();
        authRetrofitClient = retrofitClientManager.getAuthClient(application);
        loginApi = retrofitClient.create(LoginApi.class);
        authLoginApi = authRetrofitClient.create(LoginApi.class);
    }

    public LoginRepository(Retrofit retrofitClient, LoginApi loginApi){
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        this.retrofitClient = retrofitClient;
        this.loginApi = loginApi;
    }

    public static LoginRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LoginRepository(application);
        }
        return instance;
    }

    public void login(String username, String password, com.example.moum.utils.Callback<Result<Token>> callback) {
        RequestBody username1 = RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody password1 = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        Call<SuccessResponse<Integer>> result = loginApi.login(username1, password1);
        result.enqueue(new Callback<SuccessResponse<Integer>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Integer>> call, Response<SuccessResponse<Integer>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    String header = response.headers().get("access");
                    Log.e(TAG, "Header: " + header);

                    List<String> cookies = response.headers().values("Set-Cookie");
                    for (String cookie : cookies) {
                        Log.e(TAG, "Cookie: " + cookie);
                    }
                    SuccessResponse<Integer> responseBody = response.body();
                    assert responseBody != null;
                    Integer id = responseBody.getData();
                    Token token = new Token(header, cookies.get(0).substring(8), id);

                    Log.e(TAG, responseBody.toString());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Token> result = new Result<Token>(validation, token);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Token> result = new Result<Token>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Integer>> call, Throwable t) {
                Result<Token> result = new Result<Token>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void logout(com.example.moum.utils.Callback<Result<Object>> callback) {
        Call<SuccessResponse<Object>> result = authLoginApi.logout();
        result.enqueue(new Callback<SuccessResponse<Object>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Object>> call, Response<SuccessResponse<Object>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Object> responseBody = response.body();
                    assert responseBody != null;
                    Log.e(TAG, responseBody.toString());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Object> result = new Result<>(validation);
                    callback.onResult(result);

                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Object> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<SuccessResponse<Object>> call, Throwable t) {
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

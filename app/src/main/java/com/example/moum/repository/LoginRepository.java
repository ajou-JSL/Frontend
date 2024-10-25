package com.example.moum.repository;

import android.util.Log;

import com.example.moum.data.api.LoginApi;
import com.example.moum.data.api.SignupApi;
import com.example.moum.data.dto.ErrorDetail;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.LoginRequest;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SignupResponse;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.Token;
import com.example.moum.data.entity.User;
import com.example.moum.utils.Validation;
import com.example.moum.utils.ValueMap;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginRepository {
    private static LoginRepository instance;
    private LoginApi loginApi;
    private Retrofit retrofitClient;
    private String TAG = getClass().toString();

    private LoginRepository() {
        retrofitClient = RetrofitClient.getClient();
        loginApi = retrofitClient.create(LoginApi.class);
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public void login(String email, String password, com.example.moum.utils.Callback<Result<Token>> callback) {

        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<SuccessResponse> result = loginApi.login(loginRequest);
        result.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    String header = response.headers().get("access");
                    Log.e(TAG, "Header: " + header);

                    List<String> cookies = response.headers().values("Set-Cookie");
                    for (String cookie : cookies) {
                        Log.e(TAG, "Cookie: " + cookie);
                    }
                    Token token = new Token(header, cookies.get(0));

                    SuccessResponse responseBody = response.body();
                    Log.e(TAG, "Status: " + responseBody.getStatus() + "Code: " + responseBody.getCode() + "Message: " + responseBody.getMessage() + "Data: " + responseBody.getData());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Token> result = new Result<Token>(validation, token);
                    callback.onResult(result);

                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            for (ErrorDetail error : errorResponse.getErrors()) {
                                Log.e(TAG, "Field: " + error.getField() + " Reason: " + error.getReason());
                            }
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
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Result<Token> result = new Result<Token>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

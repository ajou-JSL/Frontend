package com.example.moum.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.dto.EmailAuthRequest;
import com.example.moum.data.dto.EmailCodeRequest;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Result;
import com.example.moum.data.entity.SignupUser;
import com.example.moum.repository.client.BaseUrl;
import com.example.moum.repository.client.RetrofitClientManager;
import com.example.moum.utils.ValueMap;
import com.example.moum.utils.Validation;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRepository {
    private static SignupRepository instance;
    private RetrofitClientManager retrofitClientManager;
    private SignupApi signupApi;
    private SignupApi authSignupApi;
    private Retrofit retrofitClient;
    private Retrofit authRetrofitClient;
    private String TAG = getClass().toString();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public SignupRepository(Application application) {
        retrofitClientManager = new RetrofitClientManager();
        retrofitClientManager.setBaseUrl(BaseUrl.BASIC_SERVER_PATH.getUrl());
        retrofitClient = retrofitClientManager.getClient();
        authRetrofitClient = retrofitClientManager.getAuthClient(application);
        signupApi = retrofitClient.create(SignupApi.class);
        authSignupApi = authRetrofitClient.create(SignupApi.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public SignupRepository(RetrofitClientManager retrofitClientManager) {
        this.retrofitClientManager = retrofitClientManager;
        retrofitClient = retrofitClientManager.getClient();
        authRetrofitClient = retrofitClientManager.getAuthClient(null);
        signupApi = retrofitClient.create(SignupApi.class);
        authSignupApi = authRetrofitClient.create(SignupApi.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static SignupRepository getInstance(Application application) {
        if (instance == null) {
            instance = new SignupRepository(application);
        }
        return instance;
    }

    public void emailAuth(String email, com.example.moum.utils.Callback<Result<Object>> callback) {

        EmailAuthRequest emailAuthRequest = new EmailAuthRequest(email);
        Call<SuccessResponse<String>> result = signupApi.emailAuth(emailAuthRequest);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
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
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                /*요청과 응답에 실패했을 때*/
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });

    }

    public void checkEmailCode(String email, String verifyCode, com.example.moum.utils.Callback<Result<Object>> callback) {

        EmailCodeRequest emailCodeRequest = new EmailCodeRequest(email, verifyCode);
        Call<SuccessResponse<String>> result = signupApi.checkEmailCode(emailCodeRequest);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<String> responseBody = response.body();
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
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void signup(SignupUser signupUser, com.example.moum.utils.Callback<Result<Object>> callback) {

        SignupRequest signupRequest = new SignupRequest(
                signupUser.getUsername(),
                signupUser.getPassword(),
                signupUser.getEmail(),
                signupUser.getName(),
                signupUser.getProfileDescription(),
                signupUser.getInstrument(),
                signupUser.getProficiency(),
                signupUser.getAddress(),
                signupUser.getEmailCode(),
                signupUser.getRecords(),
                signupUser.getVideoUrl(),
                signupUser.getGenres()
        );

        MultipartBody.Part profileImage = null;
        if (signupUser.getProfileImage() != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), signupUser.getProfileImage());
            profileImage = MultipartBody.Part.createFormData("profileImage", signupUser.getProfileImage().getName(), requestFile);
        } else {
            profileImage = MultipartBody.Part.createFormData("file", null, null);
        }

        Call<SuccessResponse<String>> result = signupApi.signup(signupRequest, profileImage);
        result.enqueue(new Callback<SuccessResponse<String>>() {
            @Override
            public void onResponse(Call<SuccessResponse<String>> call, Response<SuccessResponse<String>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse responseBody = response.body();
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
            public void onFailure(Call<SuccessResponse<String>> call, Throwable t) {
                Result<Object> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

    public void signout(com.example.moum.utils.Callback<Result<Member>> callback) {
        Call<SuccessResponse<Member>> result = authSignupApi.signout();
        result.enqueue(new Callback<SuccessResponse<Member>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Member>> call, Response<SuccessResponse<Member>> response) {
                if (response.isSuccessful()) {
                    /*성공적으로 응답을 받았을 때*/
                    SuccessResponse<Member> responseBody = response.body();
                    Member member = responseBody.getData();
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    Result<Member> result = new Result<>(validation, member);
                    callback.onResult(result);
                } else {
                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            Log.e(TAG, errorResponse.toString());
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            Result<Member> result = new Result<>(validation);
                            callback.onResult(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Member>> call, Throwable t) {
                Result<Member> result = new Result<>(Validation.NETWORK_FAILED);
                callback.onResult(result);
            }
        });
    }

}

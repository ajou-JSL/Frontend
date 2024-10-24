package com.example.moum.repository;

import android.net.Uri;
import android.util.Log;

import com.example.moum.data.api.SignupApi;
import com.example.moum.data.dto.EmailAuthRequest;
import com.example.moum.data.dto.EmailAuthResponse;
import com.example.moum.data.dto.EmailCodeRequest;
import com.example.moum.data.dto.EmailCodeResponse;
import com.example.moum.data.dto.ErrorDetail;
import com.example.moum.data.dto.ErrorResponse;
import com.example.moum.data.dto.SignupRequest;
import com.example.moum.data.dto.SignupResponse;
import com.example.moum.data.entity.User;
import com.example.moum.utils.ValueMap;
import com.example.moum.utils.Validation;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRepository {
    private static SignupRepository instance;
    private SignupApi signupApi;
    private Retrofit retrofitClient;
    private String TAG = getClass().toString();

    private SignupRepository() {
        retrofitClient = RetrofitClient.getClient();
        signupApi = retrofitClient.create(SignupApi.class);
    }

    public static SignupRepository getInstance() {
        if (instance == null) {
            instance = new SignupRepository();
        }
        return instance;
    }

    public void emailAuth(String email, com.example.moum.utils.Callback<Validation> callback) {

        EmailAuthRequest emailAuthRequest = new EmailAuthRequest(email);
        Call<EmailAuthResponse> result = signupApi.emailAuth(emailAuthRequest);
        result.enqueue(new Callback<EmailAuthResponse>() {
            @Override
            public void onResponse(Call<EmailAuthResponse> call, Response<EmailAuthResponse> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    EmailAuthResponse responseBody = response.body();
                    Log.d(TAG, "status: " + responseBody.getStatus() + "code: " + responseBody.getCode() + "message: " + responseBody.getMessage() + "data: " + responseBody.getData());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    callback.onResult(validation);

                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            for (ErrorDetail error : errorResponse.getErrors()) {
                                Log.d(TAG, "Field: " + error.getField() + " Reason: " + error.getReason());
                            }
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            callback.onResult(validation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailAuthResponse> call, Throwable t) {

                /*요청과 응답에 실패했을 때*/
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });

    }

    public void checkEmailCode(String email, String verifyCode, com.example.moum.utils.Callback<Validation> callback) {

        EmailCodeRequest emailCodeRequest = new EmailCodeRequest(email, verifyCode);
        Call<EmailCodeResponse> result = signupApi.checkEmailCode(emailCodeRequest);
        result.enqueue(new Callback<EmailCodeResponse>() {
            @Override
            public void onResponse(Call<EmailCodeResponse> call, Response<EmailCodeResponse> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    EmailCodeResponse responseBody = response.body();
                    Log.d(TAG, "status: " + responseBody.getStatus() + "code: " + responseBody.getCode() + "message: " + responseBody.getMessage() + "data: " + responseBody.getData());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    callback.onResult(validation);
                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            for (ErrorDetail error : errorResponse.getErrors()) {
                                Log.d(TAG, "Field: " + error.getField() + " Reason: " + error.getReason());
                            }
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            callback.onResult(validation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmailCodeResponse> call, Throwable t) {
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });
    }

    public void signup(User user, com.example.moum.utils.Callback<Validation> callback) {

        SignupRequest signupRequest = new SignupRequest(
            user.getName(),
            user.getPassword(),
            user.getEmail(),
            user.getNickname(),
            user.getProfileDescription(),
            user.getInstrument(),
            user.getProficiency(),
            user.getAddress(),
            user.getRecords()
        );
        File file = new File(user.getProfileImage().toString());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part profileImage = MultipartBody.Part.createFormData("profileImage", file.getName(), requestFile);

        Call<SignupResponse> result = signupApi.signup(signupRequest, profileImage);
        result.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful()) {

                    /*성공적으로 응답을 받았을 때*/
                    SignupResponse responseBody = response.body();
                    Log.d(TAG, "status: " + responseBody.getStatus() + "code: " + responseBody.getCode() + "message: " + responseBody.getMessage() + "data: " + responseBody.getData());
                    Validation validation = ValueMap.getCodeToVal(responseBody.getCode());
                    callback.onResult(validation);
                } else {

                    /*응답은 받았으나 문제 발생 시*/
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                        if (errorResponse != null && errorResponse.getErrors() != null) {
                            for (ErrorDetail error : errorResponse.getErrors()) {
                                Log.d(TAG, "Field: " + error.getField() + " Reason: " + error.getReason());
                            }
                            Validation validation = ValueMap.getCodeToVal(errorResponse.getCode());
                            callback.onResult(validation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                callback.onResult(Validation.NETWORK_FAILED);
            }
        });
    }

}

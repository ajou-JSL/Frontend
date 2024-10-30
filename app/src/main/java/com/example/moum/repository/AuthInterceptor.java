package com.example.moum.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.moum.data.api.SignupApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AuthInterceptor implements Interceptor {

    private static final int CODE_TOKEN_EXPIRATION = 410;
    private String accessToken;
    private String refreshToken;
    public String TAG = getClass().toString();
    private Context context;
    //private SharedPreferenceManager sharedPreferenceManager;

    public AuthInterceptor(Context context, String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.context = context;
        //this.sharedPreferenceManager = new SharedPreferenceManager(context, context.getString(R.string.preference_file_key));
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        if (accessToken == null || originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest);
        }

        Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", accessToken)
                .header("Accept-Encoding", "gzip");

        Request newRequest = builder.build();

        //return chain.proceed(newRequest);

        Response response = chain.proceed(newRequest);

//        if(response.code() == CODE_TOKEN_EXPIRATION){
//            synchronized (this){
//
//                Log.e(TAG, "Refresh token request start");
//
//                // Refresh token을 이용해 새로운 access token을 요청
//                Retrofit retrofit = RetrofitClient.getClient();
//                SignupApi service = retrofit.create(SignupApi.class);
//                Call<SignupApi.TokenResponse> result = service.refresh(refreshToken);
//                retrofit2.Response<SignupApi.TokenResponse> refreshResponse = result.execute();
//
//                if (refreshResponse.isSuccessful()) {
//                    //만약 새 access token 발급이 성공이라면, 이전 요청을 다시 실행
//                    String newAccessToken = refreshResponse.body().getAccessToken();
//                    String newRefreshToken = refreshResponse.body().getRefreshToken();
//                    String message = refreshResponse.body().getMessage();
//                    Log.e(TAG, "refreshing is successful, access token: " + newAccessToken + " refresh token: " + newRefreshToken + " message: " + message + " code: " + refreshResponse.code());
//
//                    builder.header("Authorization", newAccessToken);
//                    response = chain.proceed(builder.build());
//
////                    sharedPreferenceManager.setCache(context.getString(R.string.user_access_token_key), newAccessToken);
////                    sharedPreferenceManager.setCache(context.getString(R.string.user_refresh_token_key), newRefreshToken);
//                    accessToken = newAccessToken;
//                    refreshToken = newRefreshToken;
//
//                } else {
//                    Log.e(TAG, "Refresh token request failed");
//                }
//            }
//        }

        return response;
    }
}


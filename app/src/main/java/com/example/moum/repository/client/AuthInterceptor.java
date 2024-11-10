package com.example.moum.repository.client;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.moum.R;
import com.example.moum.data.api.LoginApi;
import com.example.moum.data.api.SignupApi;
import com.example.moum.data.dto.RefreshRequest;
import com.example.moum.data.dto.SuccessResponse;
import com.example.moum.data.entity.Token;
import com.example.moum.utils.SharedPreferenceManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class AuthInterceptor implements Interceptor {

    private static final int CODE_TOKEN_EXPIRATION = 410; //TODO 수정 필요
    private String accessToken;
    private String refreshToken;
    public String TAG = getClass().toString();
    private Context context;
    private SharedPreferenceManager sharedPreferenceManager;
    RetrofitClientManager retrofitClientManager;

    public AuthInterceptor(Context context){
        this.context = context;
        this.sharedPreferenceManager = new SharedPreferenceManager(context, context.getString(R.string.preference_file_key));
        this.accessToken = sharedPreferenceManager.getCache(context.getString(R.string.user_access_token_key), "no-access-token");
        this.refreshToken = sharedPreferenceManager.getCache(context.getString(R.string.user_refresh_token_key), "no-refresh-token");
        this.retrofitClientManager = new RetrofitClientManager();
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        if (accessToken == null || originalRequest.header("access") != null) {
            return chain.proceed(originalRequest);
        }

        Request.Builder builder = originalRequest.newBuilder()
                .header("access", accessToken)
                .header("Accept-Encoding", "gzip");

        Request newRequest = builder.build();

        //return chain.proceed(newRequest);

        Response response = chain.proceed(newRequest);

        if(response.code() == CODE_TOKEN_EXPIRATION){
            synchronized (this){

                Log.e(TAG, "Refresh token request start");

                // Refresh token을 이용해 새로운 access token을 요청
                Retrofit client = retrofitClientManager.getClient();
                LoginApi service = client.create(LoginApi.class);
                RefreshRequest refreshRequest = new RefreshRequest(refreshToken);
                Call<SuccessResponse<String>> result = service.refresh(refreshRequest);
                retrofit2.Response<SuccessResponse<String>> refreshResponse = result.execute();

                if (refreshResponse.isSuccessful()) {
                    //만약 새 access token 발급이 성공이라면, 이전 요청을 다시 실행
                    SuccessResponse<String> refreshBody = refreshResponse.body();
                    String header = response.headers().get("access");
                    Log.e(TAG, "Header: " + header);

                    List<String> cookies = response.headers().values("Set-Cookie");
                    for (String cookie : cookies) {
                        Log.e(TAG, "Cookie: " + cookie);
                    }
                    String newAccessToken = header;
                    String newRefreshToken = cookies.get(0);
                    Log.e(TAG, refreshBody.getData().toString());
                    Log.e(TAG, "NewAccessToken :" + newAccessToken + " NewRefreshToken: " + newRefreshToken);

                    sharedPreferenceManager.setCache(context.getString(R.string.user_access_token_key), newAccessToken);
                    sharedPreferenceManager.setCache(context.getString(R.string.user_refresh_token_key), newRefreshToken);
                    accessToken = newAccessToken;
                    refreshToken = newRefreshToken;

                    builder.header("Authorization", newAccessToken);
                    response = chain.proceed(builder.build());

                } else {
                    Log.e(TAG, "Refresh token request failed");
                }
            }
        }

        return response;
    }
}
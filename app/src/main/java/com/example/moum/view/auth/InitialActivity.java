package com.example.moum.view.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moum.MainActivity;
import com.example.moum.R;
import com.example.moum.databinding.ActivityInitialBinding;
import com.example.moum.utils.SharedPreferenceManager;

public class InitialActivity extends AppCompatActivity {

    ActivityInitialBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitialActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitialActivity.this, SignupBasicActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //autoLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //autoLogin();

    }

    private void autoLogin(){
        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            return;
        }
        else{
            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
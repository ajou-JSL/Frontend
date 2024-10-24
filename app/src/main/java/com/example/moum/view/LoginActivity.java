package com.example.moum.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.ActivityLoginBinding;
import com.example.moum.viewmodel.LoginViewModel;
import com.example.moum.viewmodel.SignupViewModel;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Context context;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);

        /*이전 버튼 클릭 이벤트*/
        binding.buttonLoginReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, InitialActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*로그인 버튼 클릭 이벤트*/
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.login();
            }
        });

        /*로그인 결과 감시*/
        loginViewModel.getIsLoginSuccess().observe(this, isLoginSuccess -> {

        });

        /*placeholder에 focus시 이벤트*/
        binding.loginEdittextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.loginErrorEmail.setText("");
                    binding.placeholderLoginEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderLoginEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.loginEdittextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.loginErrorPassword.setText("");
                    binding.placeholderLoginPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderLoginPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
    }
}

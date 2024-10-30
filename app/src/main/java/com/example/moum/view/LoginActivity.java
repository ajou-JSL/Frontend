package com.example.moum.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.ActivityLoginBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.LoginViewModel;
import com.example.moum.viewmodel.SignupViewModel;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Context context;
    private LoginViewModel loginViewModel;
    private final String TAG = getClass().toString();

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

            if(isLoginSuccess == Validation.PASSWORD_NOT_WRITTEN) {
                binding.loginEdittextPassword.requestFocus();
                binding.loginErrorPassword.setText("비밀번호를 입력하세요.");
            }
            else if(isLoginSuccess == Validation.EMAIL_NOT_WRITTEN) {
                binding.loginEdittextEmail.requestFocus();
                binding.loginErrorEmail.setText("이메일을 입력하세요.");
            }
            else if(isLoginSuccess == Validation.PASSWORD_NOT_FORMAL) {
                binding.loginEdittextPassword.requestFocus();
                binding.loginErrorPassword.setText("비밀번호 형식이 유효하지 않습니다.");
            }
            else if(isLoginSuccess == Validation.EMAIL_CODE_NOT_FORMAL) {
                binding.loginEdittextEmail.requestFocus();
                binding.loginErrorEmail.setText("이메일 형식이 유효하지 않습니다.");
            }
            else if(isLoginSuccess == Validation.LOGIN_FAILED) {
                binding.loginEdittextEmail.requestFocus();
                Toast.makeText(context, "아이디 또는 비밀번호가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isLoginSuccess == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isLoginSuccess == Validation.VALID_ALL) {

                /**
                 * TO-DO: 이동할 액티비티 수정 필요
                 */
                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                startActivity(intent);
            }
            else{
                Log.e(TAG, "다음 버튼 감시 결과를 알 수 없습니다.");
            }
        });

        /*자동로그인을 위해 SharedPreference에 토큰 저장*/
        loginViewModel.getToken().observe(this, token -> {

            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
            sharedPreferenceManager.setCache(getString(R.string.user_access_token_key), token.getAccess());
            sharedPreferenceManager.setCache(getString(R.string.user_refresh_token_key), token.getRefresh());
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

package com.example.moum.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.ActivitySignupBasicBinding;
import com.example.moum.utils.Validation;
import com.example.moum.viewmodel.SignupViewModel;

public class SignupBasicActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    private ActivitySignupBasicBinding activitySignupBasicBinding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activitySignupBasicBinding = ActivitySignupBasicBinding.inflate(getLayoutInflater());
        View view = activitySignupBasicBinding.getRoot();
        setContentView(view);
        context = this;
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        activitySignupBasicBinding.setViewModel(signupViewModel);

        /*이메일 인증 버튼 이벤트*/
        activitySignupBasicBinding.buttonEmailAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.emailAuth();
            }
        });

        /*이메일 인증 버튼 결과 감시*/
        //TO-DO 경우의 수 더 추가할 것
        signupViewModel.getIsEmailAuthSuccess().observe(this, isEmailAuthSuccess -> {
            if(isEmailAuthSuccess == Validation.EMAIL_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일을 입력하세요.");
            }
            else if(isEmailAuthSuccess == Validation.EMAIL_NOT_FORMAL) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일이 유효하지 않습니다.");
            }
            else if(isEmailAuthSuccess == Validation.VALID_ALL){
                activitySignupBasicBinding.signupEdittextEmail.setEnabled(false);
                activitySignupBasicBinding.buttonEmailAuth.setVisibility(View.GONE);
                activitySignupBasicBinding.buttonEmailConfirm.setVisibility(View.VISIBLE);
                Toast.makeText(context, "이메일로 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*인증코드 확인 버튼 이벤트*/
        activitySignupBasicBinding.buttonEmailConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.validCheckEmailCode();
            }
        });

        /*인증코드 확인 버튼 결과 감시*/
        signupViewModel.getIsEmailCodeSuccess().observe(this, isEmailCodeSuccess -> {
            if(isEmailCodeSuccess == Validation.EMAIL_CODE_FAILED) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("코드가 유효하지 않습니다.");
            }
            else if(isEmailCodeSuccess == Validation.NETWORK_FAILED) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("호출에 실패하였습니다.");
            }
            else if(isEmailCodeSuccess == Validation.VALID_ALL){
                activitySignupBasicBinding.signupEdittextEmailCode.setEnabled(false);
                activitySignupBasicBinding.buttonSignupNext.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_rounded_neon_mint_bold));
                activitySignupBasicBinding.buttonSignupNext.setEnabled(true);
                Toast.makeText(context, "이메일 인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*다음 버튼 이벤트*/
        activitySignupBasicBinding.buttonSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.validCheckBasic();
                Intent intent = new Intent(SignupBasicActivity.this, SignupProfileActivity.class);
                intent.putExtra("name", signupViewModel.getUser().getValue().getName());
                intent.putExtra("password", signupViewModel.getUser().getValue().getPassword());
                intent.putExtra("passwordCheck", signupViewModel.getUser().getValue().getPasswordCheck());
                intent.putExtra("email", signupViewModel.getUser().getValue().getEmail());
                startActivity(intent);
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        activitySignupBasicBinding.signupEdittextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    activitySignupBasicBinding.signupErrorName.setText("");
                    activitySignupBasicBinding.placeholderSignupName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    activitySignupBasicBinding.placeholderSignupName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        activitySignupBasicBinding.signupEdittextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    activitySignupBasicBinding.signupErrorPassword.setText("");
                    activitySignupBasicBinding.placeholderSignupPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    activitySignupBasicBinding.placeholderSignupPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        activitySignupBasicBinding.signupEdittextPasswordCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    activitySignupBasicBinding.signupErrorPasswordCheck.setText("");
                    activitySignupBasicBinding.placeholderSignupPasswordCheck.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    activitySignupBasicBinding.placeholderSignupPasswordCheck.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        activitySignupBasicBinding.signupEdittextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    activitySignupBasicBinding.signupErrorEmail.setText("");
                    activitySignupBasicBinding.placeholderSignupEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    activitySignupBasicBinding.placeholderSignupEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        activitySignupBasicBinding.signupEdittextEmailCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    activitySignupBasicBinding.signupErrorEmailCode.setText("");
                    activitySignupBasicBinding.placeholderSignupEmailCode.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    activitySignupBasicBinding.placeholderSignupEmailCode.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });


    }

}

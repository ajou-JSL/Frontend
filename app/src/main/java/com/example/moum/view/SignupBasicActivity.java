package com.example.moum.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
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
    public String TAG = getClass().toString();

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
        signupViewModel.getIsEmailAuthSuccess().observe(this, isEmailAuthSuccess -> {
            if(isEmailAuthSuccess == Validation.EMAIL_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일을 입력하세요.");
            }
            else if(isEmailAuthSuccess == Validation.EMAIL_NOT_FORMAL) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일이 유효하지 않습니다.");
            }
            else if(isEmailAuthSuccess == Validation.EMAIL_ALREADY_AUTH){
                activitySignupBasicBinding.signupEdittextEmail.setEnabled(false);
                activitySignupBasicBinding.buttonEmailAuth.setVisibility(View.GONE);
                activitySignupBasicBinding.buttonEmailConfirm.setVisibility(View.VISIBLE);
                activitySignupBasicBinding.signupErrorEmailCode.setText("이미 인증이 완료된 이메일입니다.");
            }
            else if(isEmailAuthSuccess == Validation.VALID_ALL){
                activitySignupBasicBinding.signupEdittextEmail.setEnabled(false);
                activitySignupBasicBinding.buttonEmailAuth.setVisibility(View.GONE);
                activitySignupBasicBinding.buttonEmailAuth.setEnabled(false);
                activitySignupBasicBinding.buttonEmailConfirm.setVisibility(View.VISIBLE);
                activitySignupBasicBinding.buttonEmailConfirm.setEnabled(true);
                Toast.makeText(context, "이메일로 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "이메일 인증 버튼 감시 결과를 알 수 없습니다.");
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
            if(isEmailCodeSuccess == Validation.EMAIL_CODE_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("이메일 코드를 입력하세요.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_CODE_NOT_FORMAL) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("이메일 코드가 유효하지 않습니다.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_AUTH_NOT_TRIED) {
                Toast.makeText(context, "이메일 인증코드 전송이 정상적으로 이루어지지 않았습니다. 새로고침하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_CODE_FAILED) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("코드가 유효하지 않거나 이미 인증한 코드입니다.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_CODE_NOT_CORRECT) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("인증코드가 올바르지 않습니다.");
            }
            else if(isEmailCodeSuccess == Validation.NETWORK_FAILED) {
                activitySignupBasicBinding.signupEdittextEmailCode.requestFocus();
                activitySignupBasicBinding.signupErrorEmailCode.setText("호출에 실패하였습니다.");
            }
            else if(isEmailCodeSuccess == Validation.VALID_ALL){
                activitySignupBasicBinding.signupEdittextEmailCode.setEnabled(false);
                Toast.makeText(context, "이메일 인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*개인 정보 동의 체크박스 이벤트*/
        activitySignupBasicBinding.checkboxPersonalAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    signupViewModel.setIsPersonalAgree(Validation.VALID_ALL);
                }else{
                    signupViewModel.setIsPersonalAgree(Validation.PERSONAL_NOT_AGREE);
                }
            }
        });

        /*다음 버튼 이벤트*/
        activitySignupBasicBinding.buttonSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.validCheckBasic();
            }
        });

        /*다음 버튼 결과 감시*/
        signupViewModel.getIsBasicValid().observe(this, isBasicValid -> {

            if(isBasicValid == Validation.NOT_VALID_ANYWAY || isBasicValid == Validation.NAME_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextName.requestFocus();
                activitySignupBasicBinding.signupErrorName.setText("이름을 입력하세요.");
            }
            else if(isBasicValid == Validation.PASSWORD_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextPassword.requestFocus();
                activitySignupBasicBinding.signupErrorPassword.setText("비밀번호를 입력하세요.");
            }
            else if(isBasicValid == Validation.PASSWORD_CHECK_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextPasswordCheck.requestFocus();
                activitySignupBasicBinding.signupErrorPasswordCheck.setText("비밀번호 확인을 입력하세요.");
            }
            else if(isBasicValid == Validation.EMAIL_NOT_WRITTEN) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일을 입력하세요.");
            }
            else if(isBasicValid == Validation.NAME_NOT_FORMAL) {
                activitySignupBasicBinding.signupEdittextName.requestFocus();
                activitySignupBasicBinding.signupErrorName.setText("이름이 유효하지 않습니다.");
            }
            else if(isBasicValid == Validation.PASSWORD_NOT_FORMAL) {
                activitySignupBasicBinding.signupEdittextPassword.requestFocus();
                activitySignupBasicBinding.signupErrorPassword.setText("비밀번호가 유효하지 않습니다");
            }
            else if(isBasicValid == Validation.EMAIL_CODE_NOT_FORMAL) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일이 유효하지 않습니다.");
            }
            else if(isBasicValid == Validation.EMAIL_AUTH_NOT_TRIED) {
                activitySignupBasicBinding.signupEdittextEmail.requestFocus();
                activitySignupBasicBinding.signupErrorEmail.setText("이메일 인증이 완료되지 않았습니다.");
            }
            else if(isBasicValid == Validation.PERSONAL_NOT_AGREE) {
                Toast.makeText(context, "개인정보 활용에 동의하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isBasicValid == Validation.PASSWORD_NOT_EQUAL) {
                activitySignupBasicBinding.signupEdittextPasswordCheck.requestFocus();
                activitySignupBasicBinding.signupErrorPasswordCheck.setText("비밀번호와 비밀번호 확인이 일치하지 않습니다. ");
            }
            else if(isBasicValid == Validation.VALID_ALL) {

                /*SignupProfile로 이동*/
                Intent intent = new Intent(SignupBasicActivity.this, SignupProfileActivity.class);
                intent.putExtra("name", signupViewModel.getUser().getValue().getName());
                intent.putExtra("password", signupViewModel.getUser().getValue().getPassword());
                intent.putExtra("passwordCheck", signupViewModel.getUser().getValue().getPasswordCheck());
                intent.putExtra("email", signupViewModel.getUser().getValue().getEmail());
                startActivity(intent);
            }
            else{
                Log.e(TAG, "다음 버튼 감시 결과를 알 수 없습니다.");
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

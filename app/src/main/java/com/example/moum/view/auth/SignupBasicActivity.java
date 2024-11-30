package com.example.moum.view.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.moum.viewmodel.auth.SignupViewModel;

public class SignupBasicActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    private ActivitySignupBasicBinding binding;
    private Context context;
    public String TAG = getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // acitvity stack 쌓기
        InitialActivity initialActivity = new InitialActivity();
        initialActivity.actList().add(this);

        super.onCreate(savedInstanceState);
        binding = ActivitySignupBasicBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setViewModel(signupViewModel);

        /*이전 버튼 클릭 이벤트*/
        binding.buttonSignupReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupBasicActivity.this, InitialActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*이메일 인증 버튼 이벤트*/
        binding.buttonEmailAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.emailAuth();
            }
        });

        /*이메일 인증 버튼 결과 감시*/
        signupViewModel.getIsEmailAuthSuccess().observe(this, isEmailAuthSuccess -> {
            if(isEmailAuthSuccess == Validation.EMAIL_NOT_WRITTEN) {
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일을 입력하세요.");
            }
            else if(isEmailAuthSuccess == Validation.EMAIL_NOT_FORMAL) {
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일이 유효하지 않습니다.");
            }
            else if(isEmailAuthSuccess == Validation.EMAIL_AUTH_FAILED){
                binding.signupErrorEmailCode.setText("이미 인증이 완료된 이메일입니다.");
            }
            else if (isEmailAuthSuccess == Validation.INVALID_INPUT_VALUE){
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일이 유효하지 않습니다.");
            }
            else if ( isEmailAuthSuccess == Validation.EMAIL_AUTH_ALREADY){
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이미 가입된 이메일입니다.");
            }
            else if(isEmailAuthSuccess == Validation.EMAIL_AUTH_SUCCESS){
                binding.signupEdittextEmail.setEnabled(false);
                binding.signupEdittextEmail.setTextColor(Color.GRAY);
                binding.buttonEmailAuth.setVisibility(View.GONE);
                binding.buttonEmailAuth.setEnabled(false);
                binding.buttonEmailConfirm.setVisibility(View.VISIBLE);
                binding.buttonEmailConfirm.setEnabled(true);
                Toast.makeText(context, "이메일로 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isEmailAuthSuccess == Validation.NETWORK_FAILED) {
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("호출에 실패하였습니다.");
            }
            else{
                Toast.makeText(context, "알수 없는 감시 결과(Code: " + isEmailAuthSuccess.toString() + ")", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "이메일 인증 버튼 감시 결과를 알 수 없습니다.");
            }

        });

        /*인증코드 확인 버튼 이벤트*/
        binding.buttonEmailConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.validCheckEmailCode();
            }
        });

        /*인증코드 확인 버튼 결과 감시*/
        signupViewModel.getIsEmailCodeSuccess().observe(this, isEmailCodeSuccess -> {
            if(isEmailCodeSuccess == Validation.EMAIL_CODE_NOT_WRITTEN) {
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("이메일 코드를 입력하세요.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_CODE_NOT_FORMAL) {
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("이메일 코드가 유효하지 않습니다.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_AUTH_NOT_TRIED) {
                Toast.makeText(context, "이메일 인증코드 전송이 정상적으로 이루어지지 않았습니다. 새로고침하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_AUTH_FAILED) {
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("인증코드가 올바르지 않습니다.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_AUTH_ALREADY) {
                Toast.makeText(context, "이미 인증이 완료된 이메일입니다. 다른 메일로 가입을 시도하세요.", Toast.LENGTH_SHORT).show();
            }
            else if (isEmailCodeSuccess == Validation.INVALID_TYPE_VALUE){
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("인증코드가 올바르지 않습니다.");
            }
            else if(isEmailCodeSuccess == Validation.NETWORK_FAILED) {
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("호출에 실패하였습니다.");
            }
            else if(isEmailCodeSuccess == Validation.EMAIL_AUTH_SUCCESS){
                binding.signupEdittextEmailCode.setEnabled(false);
                binding.signupEdittextEmailCode.setTextColor(Color.GRAY);
                Toast.makeText(context, "이메일 인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "알수 없는 감시 결과(Code: " + isEmailCodeSuccess.toString() + ")", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "이메일 인증 버튼 감시 결과를 알 수 없습니다.");
            }
        });

        /*개인 정보 동의 체크박스 이벤트*/
        binding.checkboxPersonalAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        binding.buttonSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.validCheckBasic();
            }
        });

        /*다음 버튼 결과 감시*/
        signupViewModel.getIsBasicValid().observe(this, isBasicValid -> {

            if(isBasicValid == Validation.NOT_VALID_ANYWAY || isBasicValid == Validation.ID_NOT_WRITTEN) {
                binding.signupEdittextMemberId.requestFocus();
                binding.signupErrorName.setText("아이디를 입력하세요.");
            }
            else if(isBasicValid == Validation.PASSWORD_NOT_WRITTEN) {
                binding.signupEdittextPassword.requestFocus();
                binding.signupErrorPassword.setText("비밀번호를 입력하세요.");
            }
            else if(isBasicValid == Validation.PASSWORD_CHECK_NOT_WRITTEN) {
                binding.signupEdittextPasswordCheck.requestFocus();
                binding.signupErrorPasswordCheck.setText("비밀번호 확인을 입력하세요.");
            }
            else if(isBasicValid == Validation.EMAIL_NOT_WRITTEN) {
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일을 입력하세요.");
            }
            else if(isBasicValid == Validation.EMAIL_CODE_NOT_WRITTEN) {
                binding.signupEdittextEmailCode.requestFocus();
                binding.signupErrorEmailCode.setText("이메일 인증코드를 입력하세요.");
            }
            else if(isBasicValid == Validation.ID_NOT_FORMAL) {
                binding.signupEdittextMemberId.requestFocus();
                binding.signupErrorName.setText("아이디는 영문, 한글, 숫자로 구성된 3~20자입니다.");
            }
            else if(isBasicValid == Validation.PASSWORD_NOT_FORMAL) {
                binding.signupEdittextPassword.requestFocus();
                binding.signupErrorPassword.setText("비밀번호는 영문 대소문자, 숫자, 특수문자가 포함된 8~20자입니다.");
            }
            else if(isBasicValid == Validation.EMAIL_NOT_FORMAL) {
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일이 유효하지 않습니다.");
            }
            else if(isBasicValid == Validation.EMAIL_CODE_NOT_FORMAL) {
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일 코드가 유효하지 않습니다.");
            }
            else if(isBasicValid == Validation.EMAIL_AUTH_NOT_TRIED) {
                binding.signupEdittextEmail.requestFocus();
                binding.signupErrorEmail.setText("이메일 인증이 완료되지 않았습니다.");
            }
            else if(isBasicValid == Validation.PERSONAL_NOT_AGREE) {
                Toast.makeText(context, "개인정보 활용에 동의하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(isBasicValid == Validation.PASSWORD_NOT_EQUAL) {
                binding.signupEdittextPasswordCheck.requestFocus();
                binding.signupErrorPasswordCheck.setText("비밀번호와 비밀번호 확인이 일치하지 않습니다. ");
            }
            else if(isBasicValid == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(isBasicValid == Validation.VALID_ALL) {

                /*SignupProfile로 이동*/
                Intent intent = new Intent(SignupBasicActivity.this, SignupProfileActivity.class);
                intent.putExtra("memberId", signupViewModel.getSignupUser().getValue().getUsername());
                intent.putExtra("password", signupViewModel.getSignupUser().getValue().getPassword());
                intent.putExtra("email", signupViewModel.getSignupUser().getValue().getEmail());
                intent.putExtra("emailCode", signupViewModel.getSignupUser().getValue().getEmailCode());
                startActivity(intent);
            }
            else{
                Toast.makeText(context, "감시 결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*각 placeholder 포커스 시 이벤트*/
        binding.signupEdittextMemberId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorName.setText("");
                    binding.placeholderSignupMemberId.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderSignupMemberId.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorPassword.setText("");
                    binding.placeholderSignupPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderSignupPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextPasswordCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorPasswordCheck.setText("");
                    binding.placeholderSignupPasswordCheck.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderSignupPasswordCheck.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorEmail.setText("");
                    binding.placeholderSignupEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.placeholderSignupEmail.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.signupEdittextEmailCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    binding.signupErrorEmailCode.setText("");
                    binding.placeholderSignupEmailCode.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                }else{
                    binding.signupErrorEmailCode.setText("");
                    binding.placeholderSignupEmailCode.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.enter_left, R.anim.none);
    }
}

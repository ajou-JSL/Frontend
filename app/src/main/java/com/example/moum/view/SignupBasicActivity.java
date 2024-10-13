package com.example.moum.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.databinding.ActivitySignupBasicBinding;
import com.example.moum.viewmodel.SignupViewModel;

public class SignupBasicActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    private ActivitySignupBasicBinding activitySignupBasicBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activitySignupBasicBinding = ActivitySignupBasicBinding.inflate(getLayoutInflater());
        View view = activitySignupBasicBinding.getRoot();
        setContentView(view);
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        activitySignupBasicBinding.setViewModel(signupViewModel);

        /*이메일 인증 버튼 이벤트*/
        activitySignupBasicBinding.buttonEmailAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupViewModel.emailAuth();
            }
        });

    }

}

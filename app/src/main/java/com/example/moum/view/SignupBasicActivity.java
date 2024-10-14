package com.example.moum.view;

import android.content.Intent;
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

        /*다음 버튼 이벤트*/
        activitySignupBasicBinding.buttonSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupBasicActivity.this, SignupProfileActivity.class);
                intent.putExtra("name", signupViewModel.getUser().getValue().getName());
                intent.putExtra("id", signupViewModel.getUser().getValue().getId());
                intent.putExtra("password", signupViewModel.getUser().getValue().getPassword());
                intent.putExtra("passwordCheck", signupViewModel.getUser().getValue().getPasswordCheck());
                intent.putExtra("email", signupViewModel.getUser().getValue().getEmail());
                startActivity(intent);
            }
        });
    }

}

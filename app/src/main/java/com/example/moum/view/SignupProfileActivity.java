package com.example.moum.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moum.databinding.ActivitySignupBasicBinding;
import com.example.moum.databinding.ActivitySignupProfileBinding;

public class SignupProfileActivity extends AppCompatActivity {
    ActivitySignupProfileBinding activitySignupProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activitySignupProfileBinding = ActivitySignupProfileBinding.inflate(getLayoutInflater());
        View view = activitySignupProfileBinding.getRoot();
        setContentView(view);
    }
}

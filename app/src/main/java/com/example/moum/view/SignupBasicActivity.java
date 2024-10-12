package com.example.moum.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moum.databinding.ActivitySignupBasicBinding;

public class SignupBasicActivity extends AppCompatActivity {

    ActivitySignupBasicBinding activitySignupBasicBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activitySignupBasicBinding = ActivitySignupBasicBinding.inflate(getLayoutInflater());
        View view = activitySignupBasicBinding.getRoot();
        setContentView(view);
    }

}

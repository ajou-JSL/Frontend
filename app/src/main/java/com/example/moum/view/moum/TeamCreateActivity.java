package com.example.moum.view.moum;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.databinding.ActivityTeamCreateBinding;
import com.example.moum.viewmodel.auth.SignupViewModel;
import com.example.moum.viewmodel.moum.TeamCreateViewModel;

public class TeamCreateActivity extends AppCompatActivity {
    private TeamCreateViewModel viewModel;
    ActivityTeamCreateBinding binding;
    private Context context;
    public String TAG = getClass().toString();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamCreateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(TeamCreateViewModel.class);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;


    }
}

package com.example.moum.view.community;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.moum.databinding.ActivityCommunitySearchBinding;
import com.example.moum.viewmodel.community.BoardFreeWriteViewModel;

public class CommunitySearchActivity extends AppCompatActivity {

    private ActivityCommunitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BoardFreeWriteViewModel BoardFreeWriteViewModel = new ViewModelProvider(this).get(BoardFreeWriteViewModel.class);
        super.onCreate(savedInstanceState);

        binding = ActivityCommunitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarSearch);

        initBackButton();
    }

    private void initBackButton() {
        binding.leftarrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
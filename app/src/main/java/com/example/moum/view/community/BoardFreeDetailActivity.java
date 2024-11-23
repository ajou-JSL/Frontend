package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.databinding.ActivityBoardFreeDetailBinding;
import com.example.moum.databinding.ActivityBoardFreeWriteBinding;
import com.example.moum.viewmodel.community.BoardFreeDetailViewModel;
import com.example.moum.viewmodel.community.BoardFreeWriteViewModel;

public class BoardFreeDetailActivity extends AppCompatActivity {

    private ActivityBoardFreeDetailBinding binding;
    private BoardFreeDetailViewModel viewModel;
    private int targetBoardId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BoardFreeDetailViewModel.class);
        super.onCreate(savedInstanceState);

        binding = ActivityBoardFreeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        targetBoardId = intent.getIntExtra("targetBoardId", -1);

        if (targetBoardId < 0) {
            Toast.makeText(this, "잘못된 게시물입니다.", Toast.LENGTH_SHORT).show();
            finish();  // 잘못된 경우 Activity 종료
            return;
        }

        initleftArrow();
    }

    public void initleftArrow(){
        binding.leftarrow.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}

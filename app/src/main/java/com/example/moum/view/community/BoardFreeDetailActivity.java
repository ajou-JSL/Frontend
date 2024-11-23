package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.ActivityBoardFreeDetailBinding;
import com.example.moum.databinding.ActivityBoardFreeWriteBinding;
import com.example.moum.viewmodel.community.BoardFreeDetailViewModel;
import com.example.moum.viewmodel.community.BoardFreeWriteViewModel;

public class BoardFreeDetailActivity extends AppCompatActivity {

    private ActivityBoardFreeDetailBinding binding;
    private BoardFreeDetailViewModel viewModel;
    private int targetBoardId;
    private ToggleButton wishlistButton;
    private Context context;

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

        initLeftArrow();
        initWishlistButton();
        initMenu();

    }

    public void initLeftArrow(){
        binding.leftarrow.setOnClickListener(v -> {
            finish();
        });
    }

    private void initWishlistButton() {
        wishlistButton = findViewById(R.id.Wishlist);
        wishlistButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Wishlist", "On 상태");
                } else {
                    Log.d("Wishlist", "Off 상태");
                }
            }
        });
    }

    private void initMenu() {
        binding.menu.setOnClickListener(v -> {
            Intent intent = new Intent(this, BoardFreeWriteActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}

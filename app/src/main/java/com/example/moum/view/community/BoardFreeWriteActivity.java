package com.example.moum.view.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.ActivityBoardFreeWriteBinding;
import com.example.moum.viewmodel.community.BoardFreeWriteViewModel;

public class BoardFreeWriteActivity extends AppCompatActivity {

    private ActivityBoardFreeWriteBinding binding;
    private BoardFreeWriteViewModel BoardFreeWriteViewModel;

    // 이미지 선택 결과를 처리할 ActivityResultLauncher 선언
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            addImageView(imageUri);
                        }
                    } else {
                        Uri imageUri = data.getData();
                        addImageView(imageUri);
                    }
                }
            });

    // 권한 요청을 위한 ActivityResultLauncher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // 권한이 허용되었을 때 갤러리 열기
                    openGallery();
                } else {
                    Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BoardFreeWriteViewModel = new ViewModelProvider(this).get(BoardFreeWriteViewModel.class);
        super.onCreate(savedInstanceState);

        binding = ActivityBoardFreeWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinner();
        initBackButton();
        initImageUploadButton();
    }

    private void initSpinner() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.board_free_spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.boardFreeWriteSpinner.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.boardFreeWriteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initBackButton() {
        binding.leftarrow.setOnClickListener(v -> finish());
    }

    private void initImageUploadButton() {
        binding.BoardFreeImageUpload.setOnClickListener(v -> {
            // 권한이 확인
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13 이상에서는 READ_MEDIA_IMAGES 권한 요청
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else {
                // Android 12 이하에서는 READ_EXTERNAL_STORAGE 권한 요청
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });
    }


    private void openGallery() {
        // 갤러리에서 이미지 선택을 위한 인텐트 호출
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 다중 선택 허용
        pickImageLauncher.launch(intent);
    }

    // 동적으로 ImageView 추가하는 메서드
    private void addImageView(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // 이미지 너비
                LinearLayout.LayoutParams.WRAP_CONTENT // 이미지 높이
        ));
        imageView.setImageURI(imageUri);

        // 이미지를 보여줄 LinearLayout에 추가
        binding.boardFreeImageContainer.addView(imageView, binding.boardFreeImageContainer.getChildCount() - 1);

        // ImageView 클릭 시 이미지 삭제
        imageView.setOnClickListener(v -> binding.boardFreeImageContainer.removeView(imageView));
    }

}

package com.example.moum.view.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Group;
import com.example.moum.databinding.ActivityChatCreateChatroomBinding;
import com.example.moum.databinding.ActivityChatCreateChatroomOnwardBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.chat.ChatCreateChatroomOnWardViewModel;
import com.example.moum.viewmodel.chat.ChatCreateChatroomViewModel;

import java.util.List;

public class ChatCreateChatroomOnwardActivity extends AppCompatActivity {
    private ChatCreateChatroomOnWardViewModel viewModel;
    private ActivityChatCreateChatroomOnwardBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private String accessToken;
    private String memberId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatCreateChatroomOnwardBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(ChatCreateChatroomOnWardViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_member_id_key), "no-member-id");
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*Intent로부터 전달된 데이터 받기*/
        Intent prevIntent = getIntent();
        String groupName = prevIntent.getStringExtra("groupName");
        String groupId = prevIntent.getStringExtra("groupId");

        /*이전 버튼 클릭 이벤트*/
        binding.buttonChatCreateChatroomOnwardReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이전 액티비티로 돌아가기
                finish();
            }
        });

        /*Photo picker 관련 이벤트*/
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

            if (uri != null) {
                viewModel.setProfileImage(uri);
                Log.d(TAG, "Selected URI: " + uri);
            } else {
                Log.d(TAG, "No media selected");
            }
        });
        binding.buttonImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //photo picker 띄우기
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
        viewModel.getProfileImage().observe(this, uri -> {
            Log.e(TAG, "Uri: " + uri.toString());
            Glide.with(this).load(uri).into(binding.imageviewSignupProfile);
        });

       /*참여 멤버 리사이클러뷰 표시*/

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonChatCreateChatroomOnwardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.
            }
        });
    }
}

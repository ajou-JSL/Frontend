package com.example.moum.view.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.databinding.ActivityChatBinding;
import com.example.moum.databinding.ActivityChatUpdateChatroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.ParticipantAdapter;
import com.example.moum.view.dialog.ChatroomUpdateDialog;
import com.example.moum.viewmodel.chat.ChatUpdateChatroomViewModel;
import com.example.moum.viewmodel.chat.ChatViewModel;

public class ChatUpdateChatroomActivity extends AppCompatActivity {
    private ActivityChatUpdateChatroomBinding binding;
    private ChatUpdateChatroomViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private Integer chatroomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatUpdateChatroomViewModel.class);
        binding = ActivityChatUpdateChatroomBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*채팅방 정보 이전 액티비티로부터 불러오기*/
        Intent prevIntent = getIntent();
        chatroomId = prevIntent.getIntExtra("chatroomId", -1);
        if(chatroomId == -1){
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*Photo picker 선택 후 콜백*/
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

            if (uri != null) {
                viewModel.setProfileImage(uri, false);
                Log.d(TAG, "Selected URI: " + uri);
            } else {
                Log.d(TAG, "No media selected");
            }
        });

        /*사진 업로드하기 버튼 이벤트*/
        binding.buttonImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //photo picker 띄우기
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        /*사진 업로드 감시*/
        viewModel.getProfileImage().observe(this, uri -> {
            Log.e(TAG, "Uri: " + uri.toString());
            Glide.with(this).load(uri).into(binding.imageviewProfile);
        });

        /*채팅방 정보 불러오기*/
        viewModel.loadChatroom(chatroomId);

        /*채팅방 정보 불러오기 결과 감시*/
        viewModel.getIsLoadChatroomSuccess().observe(this, isLoadChatroomSuccess -> {
            Validation validation = isLoadChatroomSuccess.getValidation();
            Chatroom chatroom = isLoadChatroomSuccess.getData();
            if(validation == Validation.CHATROOM_GET_SUCCESS){
                if(chatroom.getName() != null)
                    binding.edittextName.setText(chatroom.getName());
                if(chatroom.getFileUrl() != null && !chatroom.getFileUrl().isEmpty()){
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                            .load(chatroom.getFileUrl()).into(binding.imageviewProfile);
                    viewModel.setProfileImage(Uri.parse(chatroom.getFileUrl()), true);
                }
            }
            else if(validation == Validation.CHATROOM_GET_FAIL){
                Toast.makeText(context, "채팅방 정보 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "채팅방 정보 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });

        /*수정 버튼 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatroomUpdateDialog chatroomUpdateDialog = new ChatroomUpdateDialog(context, binding.edittextName.getText().toString());
                chatroomUpdateDialog.show();
            }
        });


        /*채팅방 수정 결과 감시*/
        viewModel.getIsUpdateChatroomSuccess().observe(this, isUpdateChatroomSuccess -> {
            Validation validation = isUpdateChatroomSuccess.getValidation();
            Chatroom chatroom = isUpdateChatroomSuccess.getData();
            if(validation == Validation.CHATROOM_UPDATE_SUCCESS){
                Toast.makeText(context, "채팅방 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("modified", 1);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
            else if(validation == Validation.CHATROOM_UPDATE_FAIL){
                Toast.makeText(context, "채팅방 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "채팅방 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });
    }

    public void onChatroomUpdateDialogYesClicked(){
        viewModel.updateChatroom(chatroomId, binding.edittextName.getText().toString(), context);
    }

}


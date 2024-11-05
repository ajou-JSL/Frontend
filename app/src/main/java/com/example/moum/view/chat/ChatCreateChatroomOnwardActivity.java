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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Group;
import com.example.moum.data.entity.User;
import com.example.moum.databinding.ActivityChatCreateChatroomBinding;
import com.example.moum.databinding.ActivityChatCreateChatroomOnwardBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.chat.ChatCreateChatroomOnWardViewModel;
import com.example.moum.viewmodel.chat.ChatCreateChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomOnwardActivity extends AppCompatActivity {
    private ChatCreateChatroomOnWardViewModel viewModel;
    private ActivityChatCreateChatroomOnwardBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private String accessToken;
    private String memberId;
    private ArrayList<User> members = new ArrayList<>();

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
        Integer groupId = prevIntent.getIntExtra("groupId", -1);

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이전 액티비티로 돌아가기
                finish();
            }
        });

        /*Photo picker 관련 이벤트*/
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

            if (uri != null) {
                viewModel.setChatroomProfile(uri);
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
        viewModel.getChatroomProfile().observe(this, uri -> {
            Log.e(TAG, "Uri: " + uri.toString());
            Glide.with(this).load(uri).into(binding.imageviewMoumtalkProfile);
        });

       /*참여 멤버 리사이클러뷰 표시*/
        RecyclerView recyclerView = binding.recyclerMoumtalkParticipants;
        ChatroomParticipantAdapter chatroomParticipantAdapter = new ChatroomParticipantAdapter();
        chatroomParticipantAdapter.setParticipants(members, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(chatroomParticipantAdapter);

        /*그룹의 멤버 리스트 조회 요청*/
        viewModel.loadMembersOfGroup(groupId);

        /*그룹의 멤버 리스트 조회 요청 결과 감시*/
        viewModel.getIsloadMembersOfGroupSuccess().observe(this, isLoadMembersOfGroupSuccess -> {
            Validation validation = isLoadMembersOfGroupSuccess.getValidation();
            //TODO: validation 추가도면 if절 더 추가
            if(validation == Validation.CHATROOM_GROUP_NOT_FOUND){
                Toast.makeText(context, "단체 설정 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패 from loadGroups()");
            }
            else if(validation == Validation.VALID_ALL) {
                members.addAll(isLoadMembersOfGroupSuccess.getData());
                chatroomParticipantAdapter.notifyItemInserted(members.size()-1);
                recyclerView.scrollToPosition(0);
            }
            else{
                Toast.makeText(context, "알 수 없는 validation", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation: " + validation.toString());
            }
        });

        /*제출 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chatroomName = binding.edittextMoumtalkName.getText().toString();
                ArrayList<Boolean> isParticipates = ChatroomParticipantAdapter.getIsParticipates();
                viewModel.setInfo(groupId, chatroomName, members, isParticipates);
                viewModel.createChatroom(context);
            }
        });

        /*제출 버튼 결과 감시*/
        viewModel.getIsCreateChatroomSuccess().observe(this, result -> {
            if(result == Validation.CHATROOM_NOT_LOADED){
                Toast.makeText(context, "채팅방 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(result == Validation.CHATROOM_NAME_EMPTY){
                binding.signupErrorMoumtalkName.setText("모음톡 이름을 입력하세요.");
                binding.edittextMoumtalkName.requestFocus();
            }
            else if(result == Validation.PARTICIPATE_AT_LEAST_TWO){
                binding.signupErrorMoumtalkParticipants.setText("멤버를 1명 이상 선택하세요.");
                binding.recyclerMoumtalkParticipants.requestFocus();
            }
            else if(result == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "알 수 없는 감시 결과", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });

    }
}

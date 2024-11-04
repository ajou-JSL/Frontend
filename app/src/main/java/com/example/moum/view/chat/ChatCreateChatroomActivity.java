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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.Group;
import com.example.moum.databinding.ActivityChatCreateChatroomBinding;
import com.example.moum.databinding.ActivitySignupProfileBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.auth.LoginActivity;
import com.example.moum.viewmodel.auth.SignupViewModel;
import com.example.moum.viewmodel.chat.ChatCreateChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomActivity extends AppCompatActivity {
    private ChatCreateChatroomViewModel chatCreateChatroomViewModel;
    private ActivityChatCreateChatroomBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private String[] groupNameList;
    private List<Group> groups;
    private SharedPreferenceManager sharedPreferenceManager;
    private String accessToken;
    private String memberId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatCreateChatroomBinding.inflate(getLayoutInflater());
        chatCreateChatroomViewModel = new ViewModelProvider(this).get(ChatCreateChatroomViewModel.class);
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

        /*이전 버튼 클릭 이벤트*/
        binding.buttonChatCreateChatroomReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이전 액티비티로 돌아가기
                finish();
            }
        });

        /*단체 리스트 받아오기*/
        //groupList = getResources().getStringArray(R.array.proficiency_list);
        chatCreateChatroomViewModel.loadGroups(memberId);

        /*단체 리스트 받아오기 결과 감시*/
        chatCreateChatroomViewModel.getIsLoadGroupSuccess().observe(this, result -> {

            groups = result.getData();
            Validation validation = result.getValidation();
            if(validation == Validation.GROUP_NOT_SELECTED){
                binding.signupErrorChatroomList.setText("단체를 선택하세요.");
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패 from loadGroups()");
            }
            else if(validation == Validation.VALID_ALL) {
                int i = 0;
                for(Group group : groups)
                    groupNameList[i] = group.getGroupName();
            }
            else{
                Toast.makeText(context, "알 수 없는 validation", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation from loadGroups()");
            }
        });

        /*group 스피너 Adapter 연결*/
        Spinner groupSpinner = binding.spinnerChatroomList;
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupNameList);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGroupName = groupNameList[position];
                for(Group group : groups)
                    if(group.getGroupName().equals(selectedGroupName)){
                        chatCreateChatroomViewModel.setSelectedGroup(group);
                        binding.signupErrorChatroomList.setText("");
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*다음 버튼 클릭 시 이벤트*/
        binding.buttonChatCreateChatroomNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = chatCreateChatroomViewModel.getSelectedGroup();
                if (group != null){
                    Intent intent = new Intent(ChatCreateChatroomActivity.this, ChatCreateChatroomOnwardActivity.class);
                    intent.putExtra("groupName", group.getGroupName());
                    intent.putExtra("groupId", group.getGroupId());
                    startActivity(intent);
                }
            }
        });

    }
}

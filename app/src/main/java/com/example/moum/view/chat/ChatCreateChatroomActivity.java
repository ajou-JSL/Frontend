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
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityChatCreateChatroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.chat.ChatCreateChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatCreateChatroomActivity extends AppCompatActivity {
    private ChatCreateChatroomViewModel chatCreateChatroomViewModel;
    private ActivityChatCreateChatroomBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private String[] teamNameList;
    private List<Team> teams = new ArrayList<>();
    private ArrayList<Team> teamsAsLeader = new ArrayList<>();
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
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
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
        chatCreateChatroomViewModel.loadTeamsAsMember(id);

        /*단체 리스트 받아오기 결과 감시*/
        chatCreateChatroomViewModel.getIsLoadTeamsAsMemberSuccess().observe(this, result -> {

            //TODO validation 더 추가해야함
            teams.clear();
            teams = result.getData();
            Validation validation = result.getValidation();
            if (validation == Validation.GROUP_NOT_SELECTED) {
                binding.signupErrorChatroomList.setText("단체를 선택하세요.");
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패 from loadGroups()");
            } else if (validation == Validation.GET_TEAM_LIST_SUCCESS && teams.isEmpty()) {
                Toast.makeText(context, "속한 단체가 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.GET_TEAM_LIST_SUCCESS) {
                int i = 0;
                teamNameList = new String[teams.size()];
                for (Team team : teams) {
                    if (team.getLeaderId().equals(id)) {
                        teamNameList[i++] = team.getTeamName();
                        teamsAsLeader.add(team);
                    }
                }

                if (teamsAsLeader.isEmpty()) {
                    Toast.makeText(context, "리더로 속한 단체가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    /*group 스피너 Adapter 연결*/
                    Spinner groupSpinner = binding.spinnerChatroomList;
                    ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNameList);
                    groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    groupSpinner.setAdapter(groupAdapter);
                    chatCreateChatroomViewModel.setSelectedGroup(teams.get(0));

                    groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            chatCreateChatroomViewModel.setSelectedGroup(teamsAsLeader.get(position));
                            binding.signupErrorChatroomList.setText("");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            return;
                        }
                    });
                }
            } else {
                Toast.makeText(context, "알 수 없는 validation", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation from loadGroups()");
            }
        });

        /*다음 버튼 클릭 시 이벤트*/
        binding.buttonChatCreateChatroomNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Team team = chatCreateChatroomViewModel.getSelectedGroup();
                if (team != null) {
                    Intent intent = new Intent(ChatCreateChatroomActivity.this, ChatCreateChatroomOnwardActivity.class);
                    intent.putExtra("teamName", team.getTeamName());
                    intent.putExtra("teamId", team.getTeamId());
                    startActivity(intent);
                } else {
                    Toast.makeText(ChatCreateChatroomActivity.this, "단체를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

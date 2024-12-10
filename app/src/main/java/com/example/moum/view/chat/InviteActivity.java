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
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityInviteBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.TeamInviteDialog;
import com.example.moum.viewmodel.chat.InviteViewModel;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends AppCompatActivity {
    private InviteViewModel viewModel;
    private ActivityInviteBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private String[] teamNameList;
    private List<Team> teams;
    private ArrayList<Team> teamsAsLeader = new ArrayList<>();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer memberId;
    private Team selectedTeam;
    private Integer targetMemberId;
    private String targetMemberName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(InviteViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*초대할 멤버 정보 불러오기*/
        Intent prevIntent = getIntent();
        targetMemberId = prevIntent.getIntExtra("targetMemberId", -1);
        targetMemberName = prevIntent.getStringExtra("targetMemberName");
        if (targetMemberId == -1) {
            Toast.makeText(context, "멤버 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이전 액티비티로 돌아가기
                finish();
            }
        });

        /*단체 리스트 받아오기*/
        viewModel.loadTeamsAsMember(memberId);

        /*단체 리스트 받아오기 결과 감시*/
        viewModel.getIsLoadTeamsAsMemberSuccess().observe(this, result -> {
            teams = result.getData();
            Validation validation = result.getValidation();
            if (validation == Validation.GROUP_NOT_SELECTED) {
                binding.inviteTeamError.setText("단체를 선택하세요.");
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패 from loadGroups()");
            } else if (validation == Validation.GET_TEAM_LIST_SUCCESS && teams.isEmpty()) {
                Toast.makeText(context, "속한 단체가 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.GET_TEAM_LIST_SUCCESS) {
                int i = 0;
                teamNameList = new String[teams.size()];
                for (Team team : teams) {
                    if (team.getLeaderId().equals(memberId)) {
                        teamNameList[i++] = team.getTeamName();
                        teamsAsLeader.add(team);
                    }
                }

                if (teamsAsLeader.isEmpty()) {
                    Toast.makeText(context, "리더로 속한 단체가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    /*group 스피너 Adapter 연결*/
                    Spinner groupSpinner = binding.spinnerInvite;
                    ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNameList);
                    groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    groupSpinner.setAdapter(groupAdapter);

                    groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedTeam = teamsAsLeader.get(position);
                            binding.inviteTeamError.setText("");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            return;
                        }
                    });
                }
            } else {
                Toast.makeText(context, "단체 리스트를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation from loadGroups()");
            }
        });

        /*다음 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validCheck(memberId, selectedTeam);
            }
        });

        /*다음 버튼 결과 감시*/
        viewModel.getIsValidCheckSuccess().observe(this, isValidCheckSuccess -> {
            if (isValidCheckSuccess == Validation.VALID_ALL) {
                // valid check 유효하다면, 최종 다이얼로그 띄우기
                TeamInviteDialog teamInviteDialog = new TeamInviteDialog(this, targetMemberName, selectedTeam.getTeamName());
                teamInviteDialog.show();
            }
        });

        /*초댇 결과 감시*/
        viewModel.getIsInviteMemberToTeamSuccess().observe(this, isInviteMemberToTeamSuccess -> {
            Validation validation = isInviteMemberToTeamSuccess.getValidation();
            Member invitedMember = isInviteMemberToTeamSuccess.getData();
            if (validation == Validation.INVITE_MEMBER_SUCCESS) {
                Toast.makeText(context, invitedMember.getName() + "을 " + selectedTeam.getTeamName() + "에 초대 완료하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else if (validation == Validation.MEMBER_NOT_EXIST) {
                Toast.makeText(context, "잘못된 멤버입니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.TEAM_NOT_FOUND) {
                Toast.makeText(context, "단체를 선택하세요.", Toast.LENGTH_SHORT).show();
                binding.inviteTeamError.setText("단체를 선택하세요.");
                binding.spinnerInvite.requestFocus();
            } else if (validation == Validation.NO_AUTHORITY) {
                Toast.makeText(context, "단체의 리더가 아닙니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "초대에 실패하였습니다", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation from loadGroups()");
            }
        });

        /*focus 이벤트*/
        binding.spinnerInvite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.inviteTeamError.setText("");
                }
            }
        });
    }

    public void onDialogYesClicked() {
        viewModel.inviteMemberToTeam(targetMemberId, selectedTeam.getTeamId());
    }
}

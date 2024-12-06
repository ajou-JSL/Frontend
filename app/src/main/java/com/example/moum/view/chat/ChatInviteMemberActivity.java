package com.example.moum.view.chat;

import static android.util.Log.e;

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
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityChatInviteMemberBinding;
import com.example.moum.databinding.ActivityChatUpdateChatroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.adapter.ChatroomInviteMemberAdapter;
import com.example.moum.view.community.adapter.ParticipantAdapter;
import com.example.moum.view.dialog.ChatroomInviteDialog;
import com.example.moum.view.dialog.ChatroomUpdateDialog;
import com.example.moum.viewmodel.chat.ChatInviteMemberViewModel;
import com.example.moum.viewmodel.chat.ChatUpdateChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatInviteMemberActivity extends AppCompatActivity {
    private ActivityChatInviteMemberBinding binding;
    private ChatInviteMemberViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private Integer chatroomId;
    private Integer teamId;
    private Chatroom.ChatroomType chatroomType;
    private final ArrayList<Member> members = new ArrayList<>();
    private ChatroomInviteMemberAdapter chatroomInviteMemberAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatInviteMemberViewModel.class);
        binding = ActivityChatInviteMemberBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*채팅방 정보 이전 액티비티로부터 불러오기*/
        Intent prevIntent = getIntent();
        chatroomId = prevIntent.getIntExtra("chatroomId", -1);
        int chatroomTypeInt = prevIntent.getIntExtra("chatroomType", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        String chatroomName = prevIntent.getStringExtra("chatroomName");
        if (chatroomId == -1 || teamId == -1 || chatroomTypeInt == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        chatroomType = Chatroom.ChatroomType.values()[chatroomTypeInt];

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*참여 멤버 리사이클러뷰 표시*/
        RecyclerView recyclerView = binding.recyclerInviteMembers;
        chatroomInviteMemberAdapter = new ChatroomInviteMemberAdapter();
        chatroomInviteMemberAdapter.setParticipants(members, id, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(chatroomInviteMemberAdapter);

        /*단체 정보 불러오기*/
        viewModel.loadTeam(teamId);

        /*단체 정보 불러오기 감시 결과*/
        viewModel.getIsLoadTeamSuccess().observe(this, isLoadTeamSuccess -> {
            Validation validation = isLoadTeamSuccess.getValidation();
            Team loadedTeam = isLoadTeamSuccess.getData();
            if (validation == Validation.GET_TEAM_SUCCESS) {
                members.clear();
                members.addAll(loadedTeam.getMembers());
                chatroomInviteMemberAdapter.notifyItemInserted(members.size() - 1);

                /*채팅방의 멤버 리스트 가져오기*/
                viewModel.loadMembersOfChatroom(chatroomId);

            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.TEAM_NOT_FOUND) {
                Toast.makeText(context, "팀을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "단체 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*채팅방의 멤버 리스트 가져오기 결과 감시*/
        viewModel.getIsLoadMembersOfChatroomSuccess().observe(this, isLoadMembersOfChatroomSuccess -> {
            Validation validation = isLoadMembersOfChatroomSuccess.getValidation();
            List<Member> loadedMembers = isLoadMembersOfChatroomSuccess.getData();
            if (validation == Validation.CHATROOM_MEMBER_LOAD_SUCCESS) {
                for (Member memberInTeam : members) {
                    boolean isAlreadyParticipate = false;
                    for (Member member : loadedMembers) {
                        if (member.getId().equals(memberInTeam.getId())) {
                            isAlreadyParticipate = true;
                            break;
                        }
                    }
                    chatroomInviteMemberAdapter.setIsAlreadyParticipate(members.indexOf(memberInTeam), isAlreadyParticipate);
                }
                chatroomInviteMemberAdapter.notifyDataSetChanged();
            } else if (validation == Validation.CHAT_RECEIVE_FAIL) {
                Toast.makeText(context, "채팅방의 멤버 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.MEMBER_NOT_EXIST) {
                Toast.makeText(context, "채팅방 내에 멤버가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });

        /*초대 버튼 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validCheck(chatroomInviteMemberAdapter.getMembersToInvite());
            }
        });

        /*valid check 결과 감시*/
        viewModel.getIsValidCheckSuccess().observe(this, isValidCheckSuccess -> {
            if (isValidCheckSuccess == Validation.VALID_ALL) {
                ChatroomInviteDialog chatroomInviteDialog = new ChatroomInviteDialog(context, chatroomName,
                        chatroomInviteMemberAdapter.getMembersToInvite());
                chatroomInviteDialog.show();
            } else if (isValidCheckSuccess == Validation.MEMBER_NOT_EXIST) {
                Toast.makeText(context, "한 명 이상의 멤버를 선택하세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "멤버 초대에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "valid check 결과를 알 수 없습니다.");
            }
        });

        /*초대 결과 감시*/
        viewModel.getIsInviteMemberSuccess().observe(this, isInviteMemberSuccess -> {
            Validation validation = isInviteMemberSuccess.getValidation();
            Chatroom chatroom = isInviteMemberSuccess.getData();
            if (validation == Validation.CHATROOM_INVITE_SUCCESS) {
                Toast.makeText(context, "멤버 초대에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("modified", 1);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else if (validation == Validation.CHATROOM_INVITE_FAIL) {
                Toast.makeText(context, "멤버 초대에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NOT_VALID_ANYWAY) {
                Toast.makeText(context, "멤버 초대에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "멤버 초대에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });
    }

    public void onChatroomInviteDialogYesClicked() {
        viewModel.inviteMemberIntoChatroom(chatroomId, chatroomType, chatroomInviteMemberAdapter.getMembersToInvite(), context);
    }

}


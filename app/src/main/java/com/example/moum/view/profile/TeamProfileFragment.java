package com.example.moum.view.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Record;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.FragmentMemberProfileBinding;
import com.example.moum.databinding.FragmentTeamProfileBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.ChatActivity;
import com.example.moum.view.profile.adapter.ProfileMemberAdapter;
import com.example.moum.view.profile.adapter.ProfileRecordAdapter;
import com.example.moum.view.profile.adapter.ProfileTeamAdapter;
import com.example.moum.viewmodel.profile.MemberProfileViewModel;
import com.example.moum.viewmodel.profile.TeamProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class TeamProfileFragment extends BottomSheetDialogFragment {
    private FragmentTeamProfileBinding binding;
    private TeamProfileViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Team targetTeam;
    private final ArrayList<Record> records = new ArrayList<>();
    private final ArrayList<Member> members = new ArrayList<>();
    public TeamProfileFragment(Context context){
        this.context = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTeamProfileBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(TeamProfileViewModel.class);
        context = getContext();
        View view = binding.getRoot();

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            dismiss();
        }

        /*이전 액티비티로부터의 값 가져오기*/
        int targetTeamId;
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getInt("targetTeamId") < 0){
            Toast.makeText(context, "조회하고자 하는 단체를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        targetTeamId = bundle.getInt("targetTeamId");

        /*이력 리사이클러뷰 설정*/
        RecyclerView recordsRecyclerView = binding.recyclerRecords;
        ProfileRecordAdapter recordsAdapter = new ProfileRecordAdapter();
        recordsAdapter.setRecords(records);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        recordsRecyclerView.setAdapter(recordsAdapter);
        recordsRecyclerView.suppressLayout(true);

        /*멤버 리사이클러뷰 설정*/
        RecyclerView mebersRecyclerView = binding.recyclerMembers;
        ProfileMemberAdapter membersAdapter = new ProfileMemberAdapter();
        membersAdapter.setMembers(members, context);
        mebersRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mebersRecyclerView.setAdapter(membersAdapter);

        /*단체 프로필 정보 불러오기*/
        viewModel.loadTeamProfile(targetTeamId);

        /*단체 프로필 정보 불러오기 결과 감시*/
        viewModel.getIsLoadTeamProfileSuccess().observe(getViewLifecycleOwner(), isLoadTeamProfileSuccess -> {
            Validation validation = isLoadTeamProfileSuccess.getValidation();
            Team team = isLoadTeamProfileSuccess.getData();
            if(validation == Validation.GET_TEAM_SUCCESS){
                targetTeam = team;
                if(targetTeam.getRecords() != null && !targetTeam.getRecords().isEmpty()){
                    records.addAll(targetTeam.getRecords());
                    recordsRecyclerView.suppressLayout(false);
                    recordsAdapter.notifyItemInserted(records.size()-1);
                    recordsRecyclerView.suppressLayout(true);
                }
                if(targetTeam.getMembers() != null && !targetTeam.getMembers().isEmpty()){
                    members.addAll(targetTeam.getMembers());
                    membersAdapter.notifyItemInserted(members.size()-1);
                }
                binding.textviewTeamName.setText(targetTeam.getTeamName());
                binding.textviewDescription.setText(targetTeam.getDescription());
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.background_more_rounded_gray)
                        .error(R.drawable.background_more_rounded_gray))
                        .load(targetTeam.getFileUrl()).into(binding.imageviewProfile);
            }
            else if(validation == Validation.TEAM_NOT_FOUND){
                Toast.makeText(context, "단체를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "프로필을 불러올 수 없습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*단체장과의 톡 시작하기 버튼 이벤트*/
        binding.buttonChatStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(targetTeam != null){
                    for(Member member : members){
                        if(member.getId() == targetTeam.getLeaderId()){
                            viewModel.createChatroom(member, id, context);
                        }
                    }
                }
            }
        });

        /*개인톡 시작하기 버튼 결과 감시*/
        viewModel.getIsCreateChatroomSuccess().observe(getViewLifecycleOwner(), isCreateChatroomSuccess -> {
            Validation validation = isCreateChatroomSuccess.getValidation();
            Chatroom chatroom = isCreateChatroomSuccess.getData();
            if(validation == Validation.PROFILE_NOT_LOADED){
                Toast.makeText(context, "프로필 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CHATROOM_CREATE_FAIL){
                Toast.makeText(context, "채팅방 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CHATROOM_WITH_ME){
                Toast.makeText(context, "나 자신과의 개인톡은 시작할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CHATROOM_ALREADY_EXIST || validation == Validation.CHATROOM_CREATE_SUCCESS){
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatroomId", chatroom.getId());
                intent.putExtra("chatroomName", chatroom.getName());
                intent.putExtra("chatroomType", chatroom.getType().getValue());
                intent.putExtra("teamId", chatroom.getTeamId());
                intent.putExtra("leaderId", chatroom.getLeaderId());
                intent.putExtra("lastChat", chatroom.getLastChat());
                intent.putExtra("lastTimestamp", chatroom.getLastTimestamp());
                intent.putExtra("fileUrl", chatroom.getFileUrl());
                context.startActivity(intent);
                dismiss();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "개인톡을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });

        /*설정 스피너 설정*/
        Spinner etcSpinner = binding.spinnerProfileEtc;
        String[] etcList = getResources().getStringArray(R.array.team_profile_etc_list);
        ArrayAdapter<String> etcAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, etcList);
        etcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etcSpinner.setAdapter(etcAdapter);
        etcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //TODO - 단체 신고하기 fragment 띄우기
                }
                else{
                    Log.e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetTeam = null;
        records.clear();
        members.clear();
    }
}

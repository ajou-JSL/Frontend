package com.example.moum.view.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
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
import com.example.moum.databinding.FragmentChatroomBinding;
import com.example.moum.databinding.FragmentMemberProfileBinding;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.YoutubeManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.ChatActivity;
import com.example.moum.view.chat.ChatCreateChatroomActivity;
import com.example.moum.view.chat.ChatUpdateChatroomActivity;
import com.example.moum.view.chat.adapter.ChatroomAdapter;
import com.example.moum.view.profile.adapter.ProfileRecordAdapter;
import com.example.moum.view.profile.adapter.ProfileTeamAdapter;
import com.example.moum.viewmodel.chat.ChatroomViewModel;
import com.example.moum.viewmodel.profile.MemberProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberProfileFragment extends BottomSheetDialogFragment {
    private FragmentMemberProfileBinding binding;
    private MemberProfileViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Member targetMember;
    private final ArrayList<Record> records = new ArrayList<>();
    private final ArrayList<Team> teams = new ArrayList<>();
    public MemberProfileFragment(Context context){
        this.context = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberProfileBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MemberProfileViewModel.class);
        context = requireContext();
        View view = binding.getRoot();
        view.setBackground(context.getDrawable(R.drawable.background_top_rounded_white));

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        String name = sharedPreferenceManager.getCache(getString(R.string.user_name_key), "no-memberName");
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            dismiss();
        }

        /*이전 액티비티로부터의 값 가져오기*/
        int targetMemberId;
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getInt("targetMemberId") < 0){
            Toast.makeText(context, "조회하고자 하는 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        targetMemberId = bundle.getInt("targetMemberId");

        /*이력 리사이클러뷰 설정*/
        RecyclerView recordsRecyclerView = binding.recyclerRecords;
        ProfileRecordAdapter recordsAdapter = new ProfileRecordAdapter();
        recordsAdapter.setRecords(records);
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        recordsRecyclerView.setAdapter(recordsAdapter);

        /*단체 리사이클러뷰 설정*/
        RecyclerView teamsRecyclerView = binding.recyclerTeams;
        ProfileTeamAdapter teamsAdapter = new ProfileTeamAdapter();
        teamsAdapter.setTeams(teams, context, this);
        teamsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        teamsRecyclerView.setAdapter(teamsAdapter);

        /*개인 프로필 정보 불러오기*/
        viewModel.loadMemberProfile(targetMemberId);

        /*개인 프로필 정보 불러오기 결과 감시*/
        viewModel.getIsLoadMemberProfileSuccess().observe(getViewLifecycleOwner(), isLoadMemberProfileSuccess -> {
            Validation validation = isLoadMemberProfileSuccess.getValidation();
            Member tMember = isLoadMemberProfileSuccess.getData();
            if(validation == Validation.GET_PROFILE_SUCCESS){
                targetMember = tMember;
                ArrayList<Record> sumRecords = new ArrayList<>();
                if(targetMember.getRecords() != null && !targetMember.getRecords().isEmpty())
                    sumRecords.addAll(targetMember.getRecords());
                if(targetMember.getMoumRecords() != null && !targetMember.getMoumRecords().isEmpty())
                    sumRecords.addAll(targetMember.getRecords());
                if(!sumRecords.isEmpty()){
                    records.clear();
                    records.addAll(sumRecords);
                    recordsAdapter.notifyDataSetChanged();
                }
                if(targetMember.getTeams() != null && !targetMember.getTeams().isEmpty()){
                    teams.clear();
                    teams.addAll(targetMember.getTeams());
                    teamsAdapter.notifyDataSetChanged();
                }
                binding.textviewNickname.setText(targetMember.getName());
                binding.textviewDescription.setText(targetMember.getProfileDescription());
                binding.textviewLocation.setText(String.format("%s(%s) | %s", targetMember.getInstrument(), targetMember.getProficiency(), targetMember.getAddress()));
                if(ImageManager.isUrlValid(targetMember.getProfileImageUrl()))
                    Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.background_circle_gray_size_fit)
                        .error(R.drawable.background_circle_gray_size_fit))
                        .load(targetMember.getProfileImageUrl())
                        .into(binding.imageviewProfile);
                int color = context.getColor(R.color.bronze);
                if(targetMember.getTier().equals("BRONZE")){
                    color = context.getColor(R.color.bronze);
                }
                else if(targetMember.getTier().equals("SILVER")){
                    color = context.getColor(R.color.silver);
                }
                else if(targetMember.getTier().equals("GOLD")){
                    color = context.getColor(R.color.gold);
                }
                else if(targetMember.getTier().equals("PLATINUM")){
                    color = context.getColor(R.color.platinum);
                }
                else if(targetMember.getTier().equals("DIAMOND")){
                    color = context.getColor(R.color.diamond);
                }
                binding.imageviewProfile.setBorderWidth(8);
                binding.imageviewProfile.setBorderColor(color);
                binding.imageviewTier.setColorFilter(color);

                if(targetMember.getVideoUrl() != null){
                    onVideoValid();
                }
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "프로필을 불러올 수 없습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*개인톡 시작하기 버튼 이벤트*/
        binding.buttonChatStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.createChatroom(targetMember, id, name, context);
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
            else if(validation == Validation.CHATROOM_ALREADY_EXIST){
                Toast.makeText(context, "채팅방이 이미 생성되어 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.CHATROOM_CREATE_SUCCESS){
                Toast.makeText(context, "채팅방을 생성하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatroomId", chatroom.getId());
                intent.putExtra("chatroomName", chatroom.getName());
                intent.putExtra("chatroomType", chatroom.getType().getValue());
                intent.putExtra("teamId", chatroom.getTeamId());
                intent.putExtra("leaderId", chatroom.getLeaderId());
                intent.putExtra("lastChat", chatroom.getLastChat());
                intent.putExtra("lastTimestamp", chatroom.getLastTimestamp());
                intent.putExtra("fileUrl", chatroom.getFileUrl());
                startActivity(intent);
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

        /*설정 드롭다운 추가*/
        String[] etcList = getResources().getStringArray(R.array.member_profile_etc_list);
        binding.dropdownProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, binding.dropdownProfile);
                for (int i = 0; i < etcList.length; i++) {
                    popupMenu.getMenu().add(etcList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("유저 신고하기")) {
                            //TODO
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        /*엠블럼 리사이클러뷰 설정*/
        //TODO 후순위



        return view;
    }

    public void onVideoValid(){
        /*유튜브 플레이어 연결*/
        YouTubePlayerView youTubePlayerView = binding.youtubePlayerView;
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                if(targetMember == null || targetMember.getVideoUrl() == null || targetMember.getVideoUrl().isEmpty()){
                    binding.layoutYoutube.setVisibility(View.GONE);
                    return;
                }
                String videoId = YoutubeManager.getVideoId(targetMember.getVideoUrl());
                if(videoId == null){
                    binding.layoutYoutube.setVisibility(View.GONE);
                    return;
                }
                binding.layoutYoutube.setVisibility(View.VISIBLE);
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }

    public void onProfileTeamClicked(Integer teamId){
        final TeamProfileFragment teamProfileFragment = new TeamProfileFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("targetTeamId", teamId);
        teamProfileFragment.setArguments(bundle);
        teamProfileFragment.show(getParentFragmentManager(), teamProfileFragment.getTag());
    }
}

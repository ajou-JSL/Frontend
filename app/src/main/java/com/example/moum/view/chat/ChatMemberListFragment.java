package com.example.moum.view.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Member;
import com.example.moum.databinding.FragmentChatLeftMenuBinding;
import com.example.moum.databinding.FragmentChatroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.adapter.ChatroomAdapter;
import com.example.moum.view.chat.adapter.ChatroomMemberAdapter;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.viewmodel.chat.ChatMemberListViewModel;
import com.example.moum.viewmodel.chat.ChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatMemberListFragment extends Fragment {
    private FragmentChatLeftMenuBinding binding;
    private ChatMemberListViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<Member> members = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatLeftMenuBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ChatMemberListViewModel.class);
        context = getContext();
        View view = binding.getRoot();

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        String name = sharedPreferenceManager.getCache(getString(R.string.user_name_key), "no-memberName");
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        /*이전 액티비티로부터 정보 가져오기*/
        int chatroomId;
        int leaderId;
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getInt("chatroomId") < 0 || bundle.getInt("leaderId") < 0){
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            FragmentManager manager = getParentFragmentManager();
            manager.beginTransaction().remove(ChatMemberListFragment.this).commit();
            manager.popBackStack();
        }
        chatroomId = bundle.getInt("chatroomId");
        leaderId = bundle.getInt("leaderId");

        /*멤버 리사이클러뷰 설정*/
        RecyclerView recyclerView = binding.recyclerMember;
        ChatroomMemberAdapter chatroomMemberAdapter = new ChatroomMemberAdapter();
        chatroomMemberAdapter.setMembers(members, leaderId, context, this);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setAdapter(chatroomMemberAdapter);

        /*채팅방의 멤버 리스트 가져오기*/
        viewModel.loadMembersOfChatroom(chatroomId);

        /*채팅방의 멤버 리스트 가져오기 결과 감시*/
        viewModel.getIsLoadMembersOfChatroomSuccess().observe(getViewLifecycleOwner(), isLoadMembersOfChatroomSuccess -> {
            Validation validation = isLoadMembersOfChatroomSuccess.getValidation();
            List<Member> loadedMembers = isLoadMembersOfChatroomSuccess.getData();
            if(validation == Validation.CHATROOM_MEMBER_LOAD_SUCCESS){
                members.clear();
                members.addAll(loadedMembers);
                chatroomMemberAdapter.notifyItemInserted(members.size()-1);
            }
            else if(validation == Validation.CHAT_RECEIVE_FAIL){
                Toast.makeText(context, "채팅방의 멤버 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.MEMBER_NOT_EXIST){
                Toast.makeText(context, "채팅방 내에 멤버가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅 불러오기 결과를 알 수 없습니다.");
            }
        });

        return view;
    }

    public void onProfileClicked(Integer targetMemberId){
        MemberProfileFragment memberProfileFragment = new MemberProfileFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("targetMemberId", targetMemberId);
        memberProfileFragment.setArguments(bundle);
        memberProfileFragment.show(getParentFragmentManager(), memberProfileFragment.getTag());
    }

}

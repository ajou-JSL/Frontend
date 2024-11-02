package com.example.moum.view.bottomnavi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.data.entity.Chatroom;
import com.example.moum.databinding.FragmentMoumtalkBinding;
import com.example.moum.utils.Validation;
import com.example.moum.view.ChatroomAdapter;
import com.example.moum.viewmodel.ChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoumtalkFragment extends Fragment {

    private FragmentMoumtalkBinding binding;
    private ChatroomViewModel chatroomViewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private ArrayList<Chatroom> chatrooms = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoumtalkBinding.inflate(inflater,container, false);
        chatroomViewModel = new ViewModelProvider(requireActivity()).get(ChatroomViewModel.class);
        context = getContext();
        View view = binding.getRoot();

        /*채팅방 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerChatroom;
        ChatroomAdapter chatroomAdapter = new ChatroomAdapter();
        chatroomAdapter.setChatrooms(chatrooms, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(chatroomAdapter);

        /*채팅방 리스트 정보 불러오기*/
        /**
         * TO-DO: 이후 토큰 사용 방식으로 변경 예정
         */
        String tempMemberId = "testuser";
        chatroomViewModel.setMemberId(tempMemberId);
        chatroomViewModel.loadChatrooms();

        /*채팅방 리스트 불러오기 결과 감시*/
        chatroomViewModel.getIsLoadChatroomsSuccess().observe(getViewLifecycleOwner(), isLoadChatroomsSuccess -> {
            Validation validation = isLoadChatroomsSuccess.getValidation();
            List<Chatroom> loadChatrooms = isLoadChatroomsSuccess.getData();
            if(validation == Validation.CHAT_POST_SUCCESS){
                chatrooms.addAll(loadChatrooms);
                chatroomAdapter.notifyItemInserted(chatrooms.size()-1);
                recyclerView.scrollToPosition(0);
            }
            else if(validation == Validation.CHAT_POST_FAIL){
                Toast.makeText(context, "채팅방 리스트 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "채팅방 리시트 불러오기 결과를 알 수 없습니다.");
            }
        });

        return view;
    }
}

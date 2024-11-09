package com.example.moum.view.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.databinding.FragmentChatroomBinding;
import com.example.moum.databinding.FragmentMemberProfileBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.ChatCreateChatroomActivity;
import com.example.moum.view.chat.adapter.ChatroomAdapter;
import com.example.moum.viewmodel.chat.ChatroomViewModel;
import com.example.moum.viewmodel.profile.MemberProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MemberProfileFragment extends BottomSheetDialogFragment {
    private FragmentMemberProfileBinding binding;
    private MemberProfileViewModel memberProfileViewModel;
    private Context context;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;

    public MemberProfileFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberProfileBinding.inflate(inflater,container, false);
        memberProfileViewModel = new ViewModelProvider(requireActivity()).get(MemberProfileViewModel.class);
        context = getContext();
        View view = binding.getRoot();


        return view;
    }
}

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
    private MemberProfileViewModel viewModel;
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
        viewModel = new ViewModelProvider(requireActivity()).get(MemberProfileViewModel.class);
        context = getContext();
        View view = binding.getRoot();

        /*이전 액티비티로부터의 값 가져오기*/
        int targetMemberId;
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getInt("targetMemberId") < 0){
            Toast.makeText(context, "조회하고자 하는 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        targetMemberId = bundle.getInt("targetMemberId");

        /*개인 프로필 정보 불러오기*/
        viewModel.loadMemberProfile(targetMemberId);

        /*개인 프로필 정보 불러오기 결과 감시*/

        /*개인톡 시작하기 버튼 이벤트*/

        /*개인톡 시작하기 버튼 결과 감시*/

        /*설정 스피너 설정*/

        /*엠블럼 리사이클러뷰 설정*/

        /*이력 리사이클러뷰 설정*/

        /*단체 리사이클러뷰 설정*/


        return view;
    }
}

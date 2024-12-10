package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.dto.MemberProfileRankResponse;
import com.example.moum.databinding.FragmentBoardMemberBinding;
import com.example.moum.utils.RefreshableFragment;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardMemberItemAdapter;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.viewmodel.community.BoardMemberViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardMemberFragment extends Fragment implements RefreshableFragment {
    private FragmentBoardMemberBinding binding;
    private BoardMemberViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<MemberProfileRankResponse> members = new ArrayList<>();
    private Boolean isLoading = false;

    public static BoardMemberFragment newInstance() {
        return new BoardMemberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BoardMemberViewModel.class);
        binding = FragmentBoardMemberBinding.inflate(inflater, container, false);
        context = getContext();
        View root = binding.getRoot();

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

        /*공연 리사이클러뷰 설정*/
        RecyclerView recyclerView = binding.boardMemberRecyclerView;
        BoardMemberItemAdapter boardMemberItemAdapter = new BoardMemberItemAdapter();
        boardMemberItemAdapter.setMembers(members, context, this);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setAdapter(boardMemberItemAdapter);

        /*멤버 리스트 불러오기*/
        viewModel.loadMembers();

        /*멤버 리스트 불러오기 감시 결과*/
        viewModel.getIsLoadMembersSuccess().observe(getViewLifecycleOwner(), isLoadMembersSuccess -> {
            Validation validation = isLoadMembersSuccess.getValidation();
            List<MemberProfileRankResponse> loadedMembers = isLoadMembersSuccess.getData();
            if (validation == Validation.GET_PROFILE_SUCCESS) {
                members.clear();
                members.addAll(loadedMembers);
                boardMemberItemAdapter.notifyItemInserted(members.size() - 1);
                recyclerView.scrollToPosition(0);
                viewModel.setRecentPageNumber(members.size());
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "공연 게시글을 불러오지 못했습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*최하단에서 스크롤 시 다음 리스트 불러오기*/
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !members.isEmpty() && !isLoading) {
                    isLoading = true;
                    viewModel.loadNextMembers();
                    handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        /*다음 리스트 불러오기 결과 감시*/
        viewModel.getIsLoadNextMembersSuccess().observe(getViewLifecycleOwner(), isLoadNextMembersSuccess -> {
            Validation validation = isLoadNextMembersSuccess.getValidation();
            List<MemberProfileRankResponse> loadedMembers = isLoadNextMembersSuccess.getData();
            if (validation == Validation.GET_PROFILE_SUCCESS) {
                members.addAll(loadedMembers);
                boardMemberItemAdapter.notifyItemInserted(members.size() - 1);
                viewModel.setRecentPageNumber(loadedMembers.size());
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "공연 게시글을 불러오지 못했습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    public void onMemberClicked(Integer targetMemberId) {
        MemberProfileFragment memberProfileFragment = new MemberProfileFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("targetMemberId", targetMemberId);
        memberProfileFragment.setArguments(bundle);
        memberProfileFragment.show(getParentFragmentManager(), memberProfileFragment.getTag());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.clearPage();
    }

    @Override
    public void refreshContent() {
        viewModel.clearPage();
        viewModel.loadMembers();
    }
}
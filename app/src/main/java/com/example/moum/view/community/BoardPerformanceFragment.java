package com.example.moum.view.community;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.moum.R;
import com.example.moum.data.entity.BoardGroupItem;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.FragmentBoardGroupBinding;
import com.example.moum.databinding.FragmentBoardPerformanceBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.ChatCreateChatroomActivity;
import com.example.moum.view.chat.adapter.ChatroomAdapter;
import com.example.moum.view.community.adapter.BoardGroupItemAdapter;
import com.example.moum.view.community.adapter.BoardPerformanceItemAdapter;
import com.example.moum.viewmodel.community.BoardGroupViewModel;
import com.example.moum.viewmodel.community.BoardPerformanceViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardPerformanceFragment extends Fragment {
    private FragmentBoardPerformanceBinding binding;
    private BoardPerformanceViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<Performance> performances = new ArrayList<>();
    private Boolean isLoading = false;

    public static BoardPerformanceFragment newInstance() {
        return new BoardPerformanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BoardPerformanceViewModel.class);
        binding = FragmentBoardPerformanceBinding.inflate(inflater, container, false);
        context = getContext();
        View root = binding.getRoot();

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
            getActivity().finish();
        }

        /*스피너 설정*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.community_board_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.boardPerformanceSpinner.setAdapter(adapter);
        binding.boardPerformanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    //TODO
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });

        /*공연 리사이클러뷰 설정*/
        RecyclerView recyclerView = binding.boardPerformanceRecyclerView;
        BoardPerformanceItemAdapter boardPerformanceItemAdapter = new BoardPerformanceItemAdapter();
        boardPerformanceItemAdapter.setPerformances(performances, context, this);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setAdapter(boardPerformanceItemAdapter);

        /*공연 게시글 리스트 불러오기*/
        viewModel.loadPerformances();

        /*공연 게시글 리스트 불러오기 감시 결과*/
        viewModel.getIsLoadPerformancesSuccess().observe(getViewLifecycleOwner(), isLoadPerformancesSuccess -> {
            Validation validation = isLoadPerformancesSuccess.getValidation();
            List<Performance> loadPerforms = isLoadPerformancesSuccess.getData();
            if(validation == Validation.PERFORMANCE_LIST_GET_SUCCESS){
                performances.clear();
                performances.addAll(loadPerforms);
                boardPerformanceItemAdapter.notifyItemInserted(performances.size()-1);
                recyclerView.scrollToPosition(0);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "공연 게시글을 불러오지 못했습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*글 작성 플로팅 버튼 이벤트*/
        binding.buttonAddPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PerformanceCreateActivity.class);
                context.startActivity(intent);
            }
        });

        /*최하단에서 스크롤 시 다음 리스트 불러오기*/
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !performances.isEmpty() &&  !isLoading){
                    isLoading = true;
                    viewModel.loadNextPerformances();
                    handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        /*다음 리스트 불러오기 결과 감시*/
        viewModel.getIsLoadNextPerformancesSuccess().observe(getViewLifecycleOwner(), isLoadNextPerformancesSuccess -> {
            Validation validation = isLoadNextPerformancesSuccess.getValidation();
            List<Performance> loadPerforms = isLoadNextPerformancesSuccess.getData();
            if(validation == Validation.PERFORMANCE_LIST_GET_SUCCESS){
                performances.addAll(loadPerforms);
                boardPerformanceItemAdapter.notifyItemInserted(performances.size()-1);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "공연 게시글을 불러오지 못했습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    public void onPerformanceClicked(Integer performId){
        Intent intent = new Intent(context, PerformanceActivity.class);
        intent.putExtra("performId", performId);
        context.startActivity(intent);
    }
}
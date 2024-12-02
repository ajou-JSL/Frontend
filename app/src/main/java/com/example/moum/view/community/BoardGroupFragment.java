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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.moum.R;
import com.example.moum.data.entity.BoardGroupItem;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.FragmentBoardGroupBinding;
import com.example.moum.utils.RefreshableFragment;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.BoardGroupItemAdapter;
import com.example.moum.viewmodel.community.BoardGroupViewModel;
import com.example.moum.viewmodel.community.BoardRecruitDetailViewModel;

import java.util.ArrayList;

public class BoardGroupFragment extends Fragment implements RefreshableFragment {
    private FragmentBoardGroupBinding binding;
    private BoardGroupViewModel boardGroupViewModel;
    private SharedPreferenceManager sharedPreferenceManager;
    private BoardGroupItemAdapter adapter;
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardGroupViewModel = new ViewModelProvider(this).get(BoardGroupViewModel.class);

        binding = FragmentBoardGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();
        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context, InitialActivity.class);
            startActivity(intent1);
        }

        initSpinner();
        initRecyclerView();

        return root;
    }

    private void initSpinner() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.community_board_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.boardGroupSpinner1.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.boardGroupSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    //Toast.makeText(requireContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardGroupRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Item 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ArrayList<BoardGroupItem> initialItemList = new ArrayList<>();
        adapter = new BoardGroupItemAdapter(initialItemList);
        recyclerView.setAdapter(adapter);

        BoardGroupViewModel viewModel = new ViewModelProvider(this).get(BoardGroupViewModel.class);

        // LiveData 관찰 및 데이터 로딩
        viewModel.getBoardGroupList().observe(getViewLifecycleOwner(), teamList -> {
            Log.d("BoardGroup", "LiveData observed: " + teamList);
            ArrayList<BoardGroupItem> itemList = new ArrayList<>();

            if (teamList != null && !teamList.isEmpty()) {
                // 실제 데이터가 있을 경우 변환하여 추가
                for (Team team : teamList) {
                    BoardGroupItem item = new BoardGroupItem();
                    item.setBoardGroupItem(team.getTeamId(), team.getTeamName(), team.getDescription(), team.getFileUrl());
                    itemList.add(item);
                }
                // 데이터 로드 성공시 Log 메시지
                Log.e("BoardGroup", "팀 목록이 성공적으로 로드되었습니다.");
            } else {
                // 데이터가 비었거나 null인 경우 Log 메시지
                Log.e("BoardGroup", "팀 목록이 비어있습니다.");
            }

            requireActivity().runOnUiThread(() -> adapter.updateItemList(itemList));
        });

        // 데이터 로딩 호출
        boardGroupViewModel.LoadBoardTeamList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void refreshContent() {
        boardGroupViewModel.LoadBoardTeamList();
    }
}
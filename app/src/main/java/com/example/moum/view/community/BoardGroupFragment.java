package com.example.moum.view.community;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.moum.view.community.adapter.BoardGroupItemAdapter;
import com.example.moum.viewmodel.community.BoardGroupViewModel;

import java.util.ArrayList;

public class BoardGroupFragment extends Fragment {
    private FragmentBoardGroupBinding binding;
    private BoardGroupViewModel boardGroupViewModel;
    private BoardGroupItemAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardGroupViewModel = new ViewModelProvider(this).get(BoardGroupViewModel.class);

        binding = FragmentBoardGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        for (int i = 0; i < 2; i++) {
            BoardGroupItem item = new BoardGroupItem();
            item.setBoardGroupItem( i,"제목 " + i, "내용 짧은 글 " + i, "https://example.com/file.jpg");
            initialItemList.add(item);
        }
        adapter = new BoardGroupItemAdapter(initialItemList);
        recyclerView.setAdapter(adapter);


        // LiveData 관찰 및 데이터 로딩
        boardGroupViewModel.getBoardGroupList().observe(getViewLifecycleOwner(), teamList -> {
            ArrayList<BoardGroupItem> itemList = new ArrayList<>();

            if (teamList != null && !teamList.isEmpty()) {
                // 실제 데이터가 있을 경우 변환하여 추가
                for (Team team : teamList) {
                    BoardGroupItem item = new BoardGroupItem();
                    item.setBoardGroupItem(team.getTeamId(), team.getTeamName(), team.getDescription(), team.getFileUrl());
                    itemList.add(item);
                }
            }
            // Adapter에 데이터 설정
            adapter.updateItemList(itemList);
        });

        // 데이터 로딩 호출
        boardGroupViewModel.LoadBoardTeamList();
    }




}
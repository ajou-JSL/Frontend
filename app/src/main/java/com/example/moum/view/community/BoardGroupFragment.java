package com.example.moum.view.community;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import com.example.moum.R;
import com.example.moum.data.entity.BoardFreeItem;
import com.example.moum.data.entity.BoardGroupItem;
import com.example.moum.databinding.FragmentBoardFreeBinding;
import com.example.moum.databinding.FragmentBoardGroupBinding;
import com.example.moum.viewmodel.community.BoardFreeViewModel;
import com.example.moum.viewmodel.community.BoardGroupViewModel;

import java.util.ArrayList;

public class BoardGroupFragment extends Fragment {


    private FragmentBoardGroupBinding binding;

    public static BoardGroupFragment newInstance() {
        return new BoardGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BoardGroupViewModel boardgroupViewModel = new ViewModelProvider(this).get(BoardGroupViewModel.class);

        binding = FragmentBoardGroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initRecyclerView();

        return root;
    }


    private void initRecyclerView() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardGroupRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //item 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 데이터 준비
        ArrayList<BoardGroupItem> itemList = new ArrayList<>();

        // 데이터 추가
        for (int i = 0; i < 10; i++) {
            BoardGroupItem item = new BoardGroupItem();
            item.setBoardGroupItem("단체 이름" + i, "짧은 소개" + i);
            itemList.add(item); // itemList에 추가
        }

        // RecyclerView 어댑터 설정
        BoardGroupItemAdapter adapter = new BoardGroupItemAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

}
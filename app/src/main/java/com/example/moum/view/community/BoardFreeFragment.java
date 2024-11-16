package com.example.moum.view.community;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.moum.data.entity.BoardFreeItem;
import com.example.moum.databinding.FragmentBoardFreeBinding;
import com.example.moum.view.community.adapter.BoardFreeItemAdapter;
import com.example.moum.viewmodel.community.BoardFreeViewModel;

import java.util.ArrayList;


public class BoardFreeFragment extends Fragment {
    private FragmentBoardFreeBinding binding;
    private BoardFreeViewModel boardFreeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boardFreeViewModel = new ViewModelProvider(this).get(BoardFreeViewModel.class);

        binding = FragmentBoardFreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initSpinner();
        initRecyclerView();
        initFloatingActionButton();

        return root;
    }

    private void initSpinner() {
        // 스피너 어댑터 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.community_spinner1_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.communitySpinner1.setAdapter(adapter);

        // 항목 선택 리스너 설정
        binding.communitySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    Toast.makeText(requireContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecyclerView() {
        // RecyclerView 초기화
        RecyclerView recyclerView = binding.boardFreeRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //item 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 데이터 준비
        ArrayList<BoardFreeItem> itemList = new ArrayList<>();

        // 데이터 추가
        for (int i = 0; i < 4; i++) {
            BoardFreeItem item = new BoardFreeItem();
            item.setBoardFreeItem(i, "제목" + i, "내용 짧은 글" + i, "작성자" + i, "시간" + i);
            itemList.add(item);
        }

        // RecyclerView 어댑터 설정
        BoardFreeItemAdapter adapter = new BoardFreeItemAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    private void initFloatingActionButton() {
        binding.communityFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoardFreeWriteActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
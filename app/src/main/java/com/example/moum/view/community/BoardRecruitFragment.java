package com.example.moum.view.community;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moum.R;
import com.example.moum.viewmodel.community.BoardRecruitViewModel;

public class BoardRecruitFragment extends Fragment {

    private BoardRecruitViewModel mViewModel;

    public static BoardRecruitFragment newInstance() {
        return new BoardRecruitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board_recruit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BoardRecruitViewModel.class);
        // TODO: Use the ViewModel
    }

}
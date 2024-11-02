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
import com.example.moum.viewmodel.community.BoardPerformanceViewModel;

public class BoardPerformanceFragment extends Fragment {

    private BoardPerformanceViewModel mViewModel;

    public static BoardPerformanceFragment newInstance() {
        return new BoardPerformanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board_performance, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BoardPerformanceViewModel.class);
        // TODO: Use the ViewModel
    }

}
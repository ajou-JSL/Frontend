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
import com.example.moum.viewmodel.community.BoardGroupViewModel;

public class BoardGroupFragment extends Fragment {

    private BoardGroupViewModel mViewModel;

    public static BoardGroupFragment newInstance() {
        return new BoardGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BoardGroupViewModel.class);
        // TODO: Use the ViewModel
    }

}
package com.example.moum.view.bottomnavi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.databinding.FragmentMoumtalkBinding;
import com.example.moum.viewmodel.bottomnavi.MoumtalkViewModel;

public class MoumtalkFragment extends Fragment {

    private FragmentMoumtalkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MoumtalkViewModel moumtalkViewModel = new ViewModelProvider(this).get(MoumtalkViewModel.class);

        binding = FragmentMoumtalkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
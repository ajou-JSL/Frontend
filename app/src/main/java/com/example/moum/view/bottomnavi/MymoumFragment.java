package com.example.moum.view.bottomnavi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.databinding.FragmentMymoumBinding;
import com.example.moum.viewmodel.bottomnavi.MymoumViewModel;

public class MymoumFragment extends Fragment {

    private FragmentMymoumBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MymoumViewModel mymoumViewModel = new ViewModelProvider(this).get(MymoumViewModel.class);

        binding = FragmentMymoumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
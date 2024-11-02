package com.example.moum.view.bottomnavi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.databinding.FragmentMyInformationBinding;
//import com.example.moum.viewmodel.bottomnavi.MyInformationModel;

public class MyInformationFragment extends Fragment {

    private FragmentMyInformationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MyInformationModel myinformationModel = new ViewModelProvider(this).get(MyInformationModel.class);

        binding = FragmentMyInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
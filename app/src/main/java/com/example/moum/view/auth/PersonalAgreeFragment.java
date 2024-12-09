package com.example.moum.view.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moum.R;
import com.example.moum.databinding.FragmentPersonalAgreeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PersonalAgreeFragment extends BottomSheetDialogFragment {
    private FragmentPersonalAgreeBinding binding;
    private Context context;

    public PersonalAgreeFragment(Context context) {
        this.context = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonalAgreeBinding.inflate(inflater, container, false);
        context = requireContext();
        View view = binding.getRoot();
        view.setBackground(context.getDrawable(R.drawable.background_top_rounded_white));

        /*위가 둥근 형태로 만들기*/
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        return view;
    }
}

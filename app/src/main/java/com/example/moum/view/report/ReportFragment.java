package com.example.moum.view.report;

import android.content.Context;

import com.example.moum.databinding.FragmentMemberProfileBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.viewmodel.profile.MemberProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportFragment extends BottomSheetDialogFragment {
    private FragmentMemberProfileBinding binding;
    private MemberProfileViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    public ReportFragment(Context context) {
        this.context = context;
    }
}

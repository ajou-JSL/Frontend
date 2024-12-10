package com.example.moum.view.report;

import static com.example.moum.R.drawable;
import static com.example.moum.R.string;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.databinding.FragmentReportTeamBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.report.ReportTeamViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportTeamFragment extends BottomSheetDialogFragment {
    private FragmentReportTeamBinding binding;
    private ReportTeamViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    public ReportTeamFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportTeamBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReportTeamViewModel.class);
        context = requireContext();
        View view = binding.getRoot();
        view.setBackground(context.getDrawable(drawable.background_top_rounded_white));

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(string.user_id_key), -1);
        String name = sharedPreferenceManager.getCache(getString(string.user_name_key), "no-memberName");
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            dismiss();
        }

        /*이전 액티비티로부터의 값 가져오기*/
        int targetTeamId;
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getInt("targetTeamId") < 0) {
            Toast.makeText(context, "신고하고자 하는 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        targetTeamId = bundle.getInt("targetTeamId");

        /*라디오 그룹 세팅*/
        binding.radioGroupTeam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_report_team_1) {
                    viewModel.setType(binding.textviewReport1.getText().toString());
                } else if (checkedId == R.id.radio_report_team_2) {
                    viewModel.setType(binding.textviewReport2.getText().toString());
                } else if (checkedId == R.id.radio_report_team_3) {
                    viewModel.setType(binding.textviewReport3.getText().toString());
                }
            }
        });

        /*제출 버튼 이벤트*/
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.reportTeam(targetTeamId, id, binding.edittextDetails.getText().toString());
            }
        });

        /*제출 버튼 결과 감시*/
        viewModel.getIsReportTeamSuccess().observe(getViewLifecycleOwner(), isReportTeamSuccess -> {
            Validation validation = isReportTeamSuccess.getValidation();
            ReportTeam reportTeam = isReportTeamSuccess.getData();
            if (validation == Validation.REPORT_TEAM_SUCCESS) {
                Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            } else if (validation == Validation.REPORT_TEAM_FAIL) {
                Toast.makeText(context, "신고에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.REPORT_TEAM_ALREADY) {
                Toast.makeText(context, "이미 신고한 멤버입니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NOT_VALID_ANYWAY) {
                Toast.makeText(context, "신고 사유를 선택하세요.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "신고에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

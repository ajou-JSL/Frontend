package com.example.moum.view.report;

import static com.example.moum.R.drawable;
import static com.example.moum.R.string;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.databinding.FragmentReportTeamReplyBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.profile.TeamProfileFragment;
import com.example.moum.viewmodel.report.ReportTeamReplyViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportTeamReplyFragment extends BottomSheetDialogFragment {
    private FragmentReportTeamReplyBinding binding;
    private ReportTeamReplyViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    public ReportTeamReplyFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportTeamReplyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReportTeamReplyViewModel.class);
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
        int reportTeamId;
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getInt("reportTeamId") < 0) {
            Toast.makeText(context, "신고 대상 단체를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        reportTeamId = bundle.getInt("reportTeamId");

        /*신고 정보 불러오기*/
        viewModel.loadReportTeam(reportTeamId);

        /*신고 정보 불러오기 결과 감시*/
        viewModel.getIsLoadReportTeamSuccess().observe(getViewLifecycleOwner(), isLoadReportTeamSuccess -> {
            Validation validation = isLoadReportTeamSuccess.getValidation();
            ReportTeam reportTeam = isLoadReportTeamSuccess.getData();
            if (validation == Validation.REPORT_TEAM_GET_SUCCESS) {
                if (reportTeam.getDetails() != null) binding.edittextDetails.setText(reportTeam.getDetails());
                if (reportTeam.getReply() != null) binding.edittextReply.setText(reportTeam.getReply());
                if (reportTeam.getType() != null) {
                    if (reportTeam.getType().equals("허위 이력 또는 사칭")) {
                        binding.radioGroupTeam.check(R.id.radio_report_team_1);
                    } else if (reportTeam.getType().equals("선정성 또는 폭력적 대화")) {
                        binding.radioGroupTeam.check(R.id.radio_report_team_2);
                    } else {
                        binding.radioGroupTeam.check(R.id.radio_report_team_3);
                    }
                }
                binding.radioGroupTeam.setEnabled(false);
                binding.radioReportTeam1.setEnabled(false);
                binding.radioReportTeam2.setEnabled(false);
                binding.radioReportTeam3.setEnabled(false);
                binding.edittextReply.setEnabled(false);
                binding.buttonGotoReported.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TeamProfileFragment teamProfileFragment = new TeamProfileFragment(context);
                        Bundle bundle = new Bundle();
                        bundle.putInt("targetTeamId", reportTeam.getTeamId());
                        teamProfileFragment.setArguments(bundle);
                        teamProfileFragment.show(getParentFragmentManager(), teamProfileFragment.getTag());
                    }
                });
            } else if (validation == Validation.REPORT_NOT_FOUND) {
                Toast.makeText(context, "신고를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "신고를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });
        binding.radioGroupTeam.setEnabled(false);
        binding.edittextReply.setEnabled(false);

        return view;
    }
}

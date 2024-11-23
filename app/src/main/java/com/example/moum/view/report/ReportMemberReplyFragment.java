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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.ReportMember;
import com.example.moum.databinding.FragmentReportMemberBinding;
import com.example.moum.databinding.FragmentReportMemberReplyBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.viewmodel.report.ReportMemberReplyViewModel;
import com.example.moum.viewmodel.report.ReportMemberViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportMemberReplyFragment extends BottomSheetDialogFragment {
    private FragmentReportMemberReplyBinding binding;
    private ReportMemberReplyViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    public ReportMemberReplyFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportMemberReplyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReportMemberReplyViewModel.class);
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
        int reportMemberId;
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getInt("reportMemberId") < 0){
            Toast.makeText(context, "신고 대상 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        reportMemberId = bundle.getInt("reportMemberId");

        /*신고 정보 불러오기*/
        viewModel.loadReportMember(reportMemberId);

        /*신고 정보 불러오기 결과 감시*/
        viewModel.getIsLoadReportMemberSuccess().observe(getViewLifecycleOwner(), isLoadReportMemberSuccess -> {
            Validation validation = isLoadReportMemberSuccess.getValidation();
            ReportMember reportMember = isLoadReportMemberSuccess.getData();
            if(validation == Validation.REPORT_MEMBER_GET_SUCCESS){
                if(reportMember.getDetails() != null) binding.edittextDetails.setText(reportMember.getDetails());
                if(reportMember.getReply() != null) binding.edittextReply.setText(reportMember.getReply());
                if(reportMember.getType() != null){
                    if(reportMember.getType().equals("허위 이력 또는 사칭"))
                        binding.radioGroupMember.check(R.id.radio_report_member_1);
                    else if(reportMember.getType().equals("선정성 또는 폭력적 대화"))
                        binding.radioGroupMember.check(R.id.radio_report_member_2);
                    else
                        binding.radioGroupMember.check(R.id.radio_report_member_3);
                }
                binding.radioGroupMember.setEnabled(false);
                binding.buttonGotoReported.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MemberProfileFragment memberProfileFragment = new MemberProfileFragment(context);
                        Bundle bundle = new Bundle();
                        bundle.putInt("targetMemberId", reportMember.getMemberId());
                        memberProfileFragment.setArguments(bundle);
                        memberProfileFragment.show(getParentFragmentManager(), memberProfileFragment.getTag());
                    }
                });
            }
            else if(validation == Validation.REPORT_NOT_FOUND){
                Toast.makeText(context, "신고를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "신고를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        return  view;
    }
}

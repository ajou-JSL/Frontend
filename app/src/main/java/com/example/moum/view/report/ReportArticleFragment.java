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
import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.databinding.FragmentReportArticleBinding;
import com.example.moum.databinding.FragmentReportTeamBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.report.ReportArticleViewModel;
import com.example.moum.viewmodel.report.ReportTeamViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportArticleFragment extends BottomSheetDialogFragment {
    private FragmentReportArticleBinding binding;
    private ReportArticleViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    public ReportArticleFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportArticleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReportArticleViewModel.class);
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
        int targetArticleId;
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getInt("targetArticleId") < 0){
            Toast.makeText(context, "신고하고자 하는 멤버를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        targetArticleId = bundle.getInt("targetArticleId");

        /*라디오 그룹 세팅*/
        binding.radioGroupArticle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.radio_report_article_1)
                    viewModel.setType(binding.textviewReport1.getText().toString());
                else if(checkedId == R.id.radio_report_article_2)
                    viewModel.setType(binding.textviewReport2.getText().toString());
                else if(checkedId == R.id.radio_report_article_3)
                    viewModel.setType(binding.textviewReport3.getText().toString());
                else if(checkedId == R.id.radio_report_article_4)
                    viewModel.setType(binding.textviewReport4.getText().toString());
            }
        });

        /*제출 버튼 이벤트*/
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.reportArticle(targetArticleId, id, binding.edittextDetails.getText().toString());
            }
        });

        /*제출 버튼 결과 감시*/
        viewModel.getIsReportArticleSuccess().observe(getViewLifecycleOwner(), isReportArticleSuccess -> {
            Validation validation = isReportArticleSuccess.getValidation();
            ReportArticle reportArticle = isReportArticleSuccess.getData();
            if(validation == Validation.REPORT_ARTICLE_SUCCESS){
                Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            else if(validation == Validation.REPORT_ARTICLE_FAIL){
                Toast.makeText(context, "신고에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.REPORT_ARTICLE_ALREADY){
                Toast.makeText(context, "이미 신고한 멤버입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NOT_VALID_ANYWAY){
                Toast.makeText(context, "신고 사유를 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "신고에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return  view;
    }
}

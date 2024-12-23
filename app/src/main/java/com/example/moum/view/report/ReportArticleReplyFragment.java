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
import com.example.moum.data.entity.ReportArticle;
import com.example.moum.databinding.FragmentReportArticleReplyBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.BoardFreeDetailActivity;
import com.example.moum.viewmodel.report.ReportArticleReplyViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ReportArticleReplyFragment extends BottomSheetDialogFragment {
    private FragmentReportArticleReplyBinding binding;
    private ReportArticleReplyViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    public ReportArticleReplyFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportArticleReplyBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReportArticleReplyViewModel.class);
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
        int reportArticleId;
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getInt("reportArticleId") < 0) {
            Toast.makeText(context, "신고 대상 단체를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        reportArticleId = bundle.getInt("reportArticleId");

        /*신고 정보 불러오기*/
        viewModel.loadReportArticle(reportArticleId);

        /*신고 정보 불러오기 결과 감시*/
        viewModel.getIsLoadReportArticleSuccess().observe(getViewLifecycleOwner(), isLoadReportArticleSuccess -> {
            Validation validation = isLoadReportArticleSuccess.getValidation();
            ReportArticle reportArticle = isLoadReportArticleSuccess.getData();
            if (validation == Validation.REPORT_ARTICLE_GET_SUCCESS) {
                if (reportArticle.getDetails() != null) binding.edittextDetails.setText(reportArticle.getDetails());
                if (reportArticle.getReply() != null) binding.edittextReply.setText(reportArticle.getReply());
                if (reportArticle.getType() != null) {
                    if (reportArticle.getType().equals("개인정보 노출 및 불법 정보 게시")) {
                        binding.radioGroupArticle.check(R.id.radio_report_article_1);
                    } else if (reportArticle.getType().equals("선정성 또는 폭력성")) {
                        binding.radioGroupArticle.check(R.id.radio_report_article_2);
                    } else if (reportArticle.getType().equals("같은 내용 반복 게시")) {
                        binding.radioGroupArticle.check(R.id.radio_report_article_3);
                    } else {
                        binding.radioGroupArticle.check(R.id.radio_report_article_4);
                    }
                }
                binding.radioGroupArticle.setEnabled(false);
                binding.radioReportArticle1.setEnabled(false);
                binding.radioReportArticle2.setEnabled(false);
                binding.radioReportArticle3.setEnabled(false);
                binding.edittextReply.setEnabled(false);
                binding.buttonGotoReported.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, BoardFreeDetailActivity.class);
                        intent.putExtra("targetBoardId", reportArticle.getArticleId());
                        context.startActivity(intent);
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
        binding.radioGroupArticle.setEnabled(false);
        binding.edittextReply.setEnabled(false);

        return view;
    }
}

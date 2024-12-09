package com.example.moum.view.myinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.ReportArticle;
import com.example.moum.data.entity.ReportMember;
import com.example.moum.data.entity.ReportTeam;
import com.example.moum.databinding.ActivityMyinfoReportNQuestionListBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.myinfo.adapter.ReportAdapter;
import com.example.moum.view.report.ReportArticleReplyFragment;
import com.example.moum.view.report.ReportMemberReplyFragment;
import com.example.moum.view.report.ReportTeamReplyFragment;
import com.example.moum.viewmodel.myinfo.MyInfoReportNQuestionListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyInfoReportNQuestionListActivity extends AppCompatActivity {
    private MyInfoReportNQuestionListViewModel viewModel;
    private ActivityMyinfoReportNQuestionListBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<ReportMember> reportMembers = new ArrayList<>();
    private ArrayList<ReportTeam> reportTeams = new ArrayList<>();
    private ArrayList<ReportArticle> reportArticles = new ArrayList<>();
    private Boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyinfoReportNQuestionListBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MyInfoReportNQuestionListViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*이전 액티비티로부터 정보 가져오기*/
        Intent prevIntent = getIntent();
        String reportType = prevIntent.getStringExtra("reportType");
        if (reportType == null || reportType.isEmpty()) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*신고 리사이클러뷰 세팅*/
        RecyclerView reportRecycler = binding.recyclerReport;
        ReportAdapter reportAdapter = new ReportAdapter();
        if (reportType.equals("member")) {
            reportAdapter.setReportMembers(reportMembers, reportType, context);
            binding.reportListName.setText("멤버 신고 리스트");
        } else if (reportType.equals("team")) {
            reportAdapter.setReportTeams(reportTeams, reportType, context);
            binding.reportListName.setText("단체 신고 리스트");
        } else {
            reportAdapter.setReportArticles(reportArticles, reportType, context);
            binding.reportListName.setText("게시글 신고 리스트");
        }
        reportRecycler.setLayoutManager(new LinearLayoutManager(context));
        reportRecycler.setAdapter(reportAdapter);

        /*나의 신고 및 문의 리스트 가져오기*/
        viewModel.loadReports(reportType, id);

        /*나의 신고 및 문의 리스트 가져오기 감시 결과*/
        viewModel.getIsLoadReportMembersSuccess().observe(this, isLoadReportMembersSuccess -> {
            Validation validation = isLoadReportMembersSuccess.getValidation();
            List<ReportMember> loadedReportMembers = isLoadReportMembersSuccess.getData();
            if (validation == Validation.REPORT_MEMBER_GET_SUCCESS) {
                reportMembers.clear();
                reportMembers.addAll(loadedReportMembers);
                reportAdapter.notifyItemInserted(reportMembers.size() - 1);
                viewModel.setRecentPageNumber(reportMembers.size());
            } else if (validation == Validation.REPORT_NOT_FOUND) {
                Toast.makeText(context, "멤버 신고 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "결과를 알 수 없습니다.");
                Toast.makeText(context, "멤버 신고 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getIsLoadReportTeamsSuccess().observe(this, isLoadReportTeamsSuccess -> {
            Validation validation = isLoadReportTeamsSuccess.getValidation();
            List<ReportTeam> loadedReportTeams = isLoadReportTeamsSuccess.getData();
            if (validation == Validation.REPORT_TEAM_GET_SUCCESS) {
                reportTeams.clear();
                reportTeams.addAll(loadedReportTeams);
                reportAdapter.notifyItemInserted(reportTeams.size() - 1);
                viewModel.setRecentPageNumber(reportTeams.size());
            } else if (validation == Validation.REPORT_NOT_FOUND) {
                Toast.makeText(context, "단체 신고 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "결과를 알 수 없습니다.");
                Toast.makeText(context, "단체 신고 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getIsLoadReportArticlesSuccess().observe(this, isLoadReportArticlesSuccess -> {
            Validation validation = isLoadReportArticlesSuccess.getValidation();
            List<ReportArticle> loadedReportArticles = isLoadReportArticlesSuccess.getData();
            if (validation == Validation.REPORT_ARTICLE_GET_SUCCESS) {
                reportArticles.clear();
                reportArticles.addAll(loadedReportArticles);
                reportAdapter.notifyItemInserted(reportArticles.size() - 1);
                viewModel.setRecentPageNumber(reportArticles.size());
            } else if (validation == Validation.REPORT_NOT_FOUND) {
                Toast.makeText(context, "게시글 신고 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "결과를 알 수 없습니다.");
                Toast.makeText(context, "게시글 신고 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*최하단에서 스크롤 시 다음 리스트 불러오기*/
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper()); // 여러번 호출되는 것을 막기 위한 디바운싱
        reportRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                    if (reportType.equals("member") && reportMembers.isEmpty()) {
                        return;
                    } else if (reportType.equals("team") && reportTeams.isEmpty()) {
                        return;
                    } else if (reportType.equals("article") && reportArticles.isEmpty()) {
                        return;
                    }
                    isLoading = true;
                    viewModel.loadNextReports(reportType, id);
                    handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clearPage();
    }

    public void onReportMemberClicked(Integer reportMemberId) {
        ReportMemberReplyFragment reportMemberReplyFragment = new ReportMemberReplyFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("reportMemberId", reportMemberId);
        reportMemberReplyFragment.setArguments(bundle);
        reportMemberReplyFragment.show(getSupportFragmentManager(), reportMemberReplyFragment.getTag());
    }

    public void onReportTeamClicked(Integer reportTeamId) {
        ReportTeamReplyFragment reportTeamReplyFragment = new ReportTeamReplyFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("reportTeamId", reportTeamId);
        reportTeamReplyFragment.setArguments(bundle);
        reportTeamReplyFragment.show(getSupportFragmentManager(), reportTeamReplyFragment.getTag());
    }

    public void onReportArticleClicked(Integer reportArticleId) {
        ReportArticleReplyFragment reportArticleReplyFragment = new ReportArticleReplyFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("reportArticleId", reportArticleId);
        reportArticleReplyFragment.setArguments(bundle);
        reportArticleReplyFragment.show(getSupportFragmentManager(), reportArticleReplyFragment.getTag());
    }
}

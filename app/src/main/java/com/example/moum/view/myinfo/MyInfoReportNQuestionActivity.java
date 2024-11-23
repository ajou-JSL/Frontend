package com.example.moum.view.myinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moum.R;
import com.example.moum.databinding.ActivityMyinfoReportNQuestionBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.report.ReportFragment;

public class MyInfoReportNQuestionActivity extends AppCompatActivity {
    private ActivityMyinfoReportNQuestionBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyinfoReportNQuestionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            finish();
        }

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*멤버 신고 리스트 버튼 클릭*/
        binding.buttonMemberReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfoReportNQuestionActivity.this, MyInfoReportNQuestionListActivity.class);
                intent.putExtra("reportType", "member");
                startActivity(intent);
                finish();
            }
        });

        /*단체 신고 리스트 버튼 클릭*/
        binding.buttonTeamReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfoReportNQuestionActivity.this, MyInfoReportNQuestionListActivity.class);
                intent.putExtra("reportType", "team");
                startActivity(intent);
                finish();
            }
        });

        /*게시글 신고 리스트 버튼 클릭*/
        binding.buttonArticleReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInfoReportNQuestionActivity.this, MyInfoReportNQuestionListActivity.class);
                intent.putExtra("reportType", "article");
                startActivity(intent);
                finish();
            }
        });

        /*신고 및 문의하기 버튼 클릭*/
        binding.buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportFragment reportFragment = new ReportFragment(context);
                Bundle bundle = new Bundle();
                reportFragment.setArguments(bundle);
                reportFragment.show(getSupportFragmentManager(), reportFragment.getTag());
            }
        });
    }
}

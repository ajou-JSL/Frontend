package com.example.moum.view.moum;

import static android.util.Log.e;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Performance;
import com.example.moum.databinding.ActivityMoumPromote1Binding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.PerformanceActivity;
import com.example.moum.viewmodel.moum.MoumPromoteViewModel;

public class MoumPromote1Activity extends AppCompatActivity {
    private ActivityMoumPromote1Binding binding;
    private MoumPromoteViewModel viewModel;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer moumId;
    private Performance performance;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumPromote1Binding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumPromoteViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        moumId = prevIntent.getIntExtra("moumId", -1);
        if (moumId == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

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

        /*이전 버튼 클릭 이벤트*/
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*공연 게시판으로 이동 버튼 클릭 이벤트*/
        binding.buttonGotoPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("finish", 1);
                intent.putExtra("fragment_index", 2); // 커뮤니티 탭으로 이동 (인덱스 2)
                intent.putExtra("community_index", 4);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        /*다음 버튼 클릭 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoumPromote1Activity.this, MoumPromote2Activity.class);
                intent.putExtra("moumId", moumId);
                startActivity(intent);
            }
        });

        /*현재 모음과 연관된 공연 게시글 불러오기*/
        binding.topPerformance.setVisibility(View.GONE);
        binding.buttonGotoPerform.setVisibility(View.VISIBLE);
        viewModel.loadPerformOfMoum(moumId);

        /*현재 모음과 연관된 공연 게시글 조회 결과 감시*/
        viewModel.getIsLoadPerformOfMoumSuccess().observe(this, isLoadPerformOfMoumSuccess -> {
            Validation validation = isLoadPerformOfMoumSuccess.getValidation();
            Performance loadedPerform = isLoadPerformOfMoumSuccess.getData();
            if (validation == Validation.PERFORMANCE_GET_SUCCESS) {
                performance = loadedPerform;
                binding.topPerformance.setVisibility(View.VISIBLE);
                binding.buttonGotoPerform.setVisibility(View.GONE);
                if (performance.getPerformanceName() != null) binding.performanceName.setText(performance.getPerformanceName());
                if (performance.getPerformanceDescription() != null) binding.performanceDescription.setText(performance.getPerformanceDescription());
                String timeStr = "";
                if (performance.getPerformanceStartDate() != null) {
                    timeStr = timeStr.concat(TimeManager.strToDate(performance.getPerformanceStartDate()));
                }
                if (performance.getPerformanceEndDate() != null) {
                    timeStr = timeStr.concat(" ~ ");
                    timeStr = timeStr.concat(TimeManager.strToDate(performance.getPerformanceEndDate()));
                }
                binding.performanceTime.setText(timeStr);
                if (performance.getTeamName() != null) binding.performanceTeam.setText(performance.getTeamName());
                if (performance.getPerformanceImageUrl() != null) {
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                                    .error(R.drawable.background_more_rounded_gray_size_fit))
                            .load(performance.getPerformanceImageUrl()).into(binding.performanceImage);
                }
                binding.performanceImage.setClipToOutline(true);
                binding.topPerformance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPerformanceClicked(performance.getId());
                    }
                });
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.ILLEGAL_ARGUMENT) {
                Toast.makeText(context, "음과 연관된 공연 게시글을 먼저 생성하세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "모음과 연관된 공연 게시글 찾기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
    }

    public void onPerformanceClicked(Integer performId) {
        Intent intent = new Intent(context, PerformanceActivity.class);
        intent.putExtra("performId", performId);
        context.startActivity(intent);
    }
}

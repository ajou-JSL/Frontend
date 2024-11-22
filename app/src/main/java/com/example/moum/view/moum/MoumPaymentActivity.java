package com.example.moum.view.moum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.databinding.ActivityMoumPaymentBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.moum.MoumManageViewModel;
import com.example.moum.viewmodel.moum.MoumPaymentViewModel;

import java.util.ArrayList;

public class MoumPaymentActivity extends AppCompatActivity {
    private MoumPaymentViewModel viewModel;
    private ActivityMoumPaymentBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer moumId;
    private Integer teamId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumPaymentBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumPaymentViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        moumId = prevIntent.getIntExtra("moumId", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        if (teamId == -1 || moumId == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
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

        /*정산 모음톡 생성하기 버튼 클릭 이벤트*/
        binding.buttonMakeMoumtalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoumPaymentChatroomActivity.class);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
                finish();
            }
        });

        /*정산 추가하기 버튼 이벤트*/
        binding.buttonAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoumPaymentAddActivity.class);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
                finish();
            }
        });
    }
}

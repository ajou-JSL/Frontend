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
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.databinding.ActivityMoumPromote1Binding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.viewmodel.moum.MoumManageViewModel;

public class MoumPromote1Activity extends AppCompatActivity {
    private ActivityMoumPromote1Binding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer moumId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumPromote1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        moumId = prevIntent.getIntExtra("moumId", -1);
        if(moumId == -1){
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
    }
}

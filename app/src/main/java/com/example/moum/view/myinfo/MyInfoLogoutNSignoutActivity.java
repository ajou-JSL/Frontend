package com.example.moum.view.myinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.databinding.ActivityMyinfoLogoutNSignoutBinding;
import com.example.moum.databinding.ActivityMyinfoReportNQuestionBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.LogoutDialog;
import com.example.moum.view.report.ReportFragment;
import com.example.moum.viewmodel.myinfo.MyInfoLogoutNSignoutViewModel;
import com.example.moum.viewmodel.myinfo.MyInformationViewModel;

public class MyInfoLogoutNSignoutActivity extends AppCompatActivity {
    private ActivityMyinfoLogoutNSignoutBinding binding;
    private MyInfoLogoutNSignoutViewModel viewModel;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private static final int REQUEST_CODE = 300;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyInfoLogoutNSignoutViewModel.class);
        binding = ActivityMyinfoLogoutNSignoutBinding.inflate(getLayoutInflater());
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

        /*로그아웃 버튼 클릭*/
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*로그아웃 확인 다이얼로그 띄움*/
                LogoutDialog logoutDialog = new LogoutDialog(context);
                logoutDialog.show();
            }
        });

        /*회원탈퇴 버튼 클릭*/
        binding.buttonSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyInfoSignoutActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                finish();
            }
        });

        /*로그아웃 결과 감시*/
        viewModel.getIsLoginSuccess().observe(this, isLoginSuccess -> {
            Validation validation = isLoginSuccess.getValidation();
            if(validation == Validation.LOGOUT_SUCCESS){
                Toast.makeText(context, "성공적으로 로그아웃하였습니다.", Toast.LENGTH_SHORT).show();

                sharedPreferenceManager.removeCache(getString(R.string.user_id_key));
                sharedPreferenceManager.removeCache(getString(R.string.user_username_key));
                sharedPreferenceManager.removeCache(getString(R.string.user_access_token_key));
                sharedPreferenceManager.removeCache(getString(R.string.user_refresh_token_key));

                Intent intent = new Intent();
                intent.putExtra("finish", 1);
                setResult(RESULT_OK, intent);
                finish();
            }
            else if(validation == Validation.LOGOUT_ALREADY){
                Toast.makeText(context, "이미 로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "로그아웃할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLogoutDialogYesClicked(){
        viewModel.logout();
    }
}

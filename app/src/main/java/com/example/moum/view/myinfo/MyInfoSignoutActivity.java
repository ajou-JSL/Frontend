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
import com.example.moum.data.entity.Member;
import com.example.moum.databinding.ActivityMyinfoLogoutNSignoutBinding;
import com.example.moum.databinding.ActivityMyinfoSignoutBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.SignoutDialog;
import com.example.moum.viewmodel.myinfo.MyInfoLogoutNSignoutViewModel;
import com.example.moum.viewmodel.myinfo.MyInfoSignoutViewModel;

public class MyInfoSignoutActivity extends AppCompatActivity {
    private ActivityMyinfoSignoutBinding binding;
    private MyInfoSignoutViewModel viewModel;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyInfoSignoutViewModel.class);
        binding = ActivityMyinfoSignoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
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

        /*제출 버튼 클릭 이벤트*/
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignoutDialog signoutDialog = new SignoutDialog(context);
                signoutDialog.show();
            }
        });

        /*회원탈퇴 결과 감시*/
        viewModel.getIsSignoutSuccess().observe(this, isSignoutSuccess -> {
            Validation validation = isSignoutSuccess.getValidation();
            Member member = isSignoutSuccess.getData();
            if(validation == Validation.ID_NOT_WRITTEN){
                Toast.makeText(context, "아이디를 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ID_NOT_EQUAL){
                Toast.makeText(context, "아이디가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }//TODO 추가 필요
            else{
                Toast.makeText(context, "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public void onSignoutDialogYesClicked(){
        viewModel.signout(username);
    }
}

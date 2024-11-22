package com.example.moum.view.moum;

import static android.util.Log.e;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Promote;
import com.example.moum.databinding.ActivityMoumPromote1Binding;
import com.example.moum.databinding.ActivityMoumPromote2Binding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.DownloadDialog;
import com.example.moum.viewmodel.moum.MoumManageViewModel;
import com.example.moum.viewmodel.moum.MoumPromoteViewModel;

public class MoumPromote2Activity extends AppCompatActivity {
    private MoumPromoteViewModel viewModel;
    private ActivityMoumPromote2Binding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer moumId;
    private Performance performance;
    private Promote promote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumPromote2Binding.inflate(getLayoutInflater());
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

        /*현재 모음과 연관된 공연 게시글 불러오기*/
        binding.buttonMakeQr.setEnabled(false);
        binding.buttonDownload.setEnabled(false);
        viewModel.loadPerformOfMoum(moumId);

        /*현재 모음과 연관된 공연 게시글 조회 결과 감시*/
        viewModel.getIsLoadPerformOfMoumSuccess().observe(this, isLoadPerformOfMoumSuccess -> {
            Validation validation = isLoadPerformOfMoumSuccess.getValidation();
            Performance loadedPerform = isLoadPerformOfMoumSuccess.getData();
            if(validation == Validation.PERFORMANCE_GET_SUCCESS){
                performance = loadedPerform;
                binding.buttonMakeQr.setEnabled(true);
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "모음과 연관된 공연 게시글 찾기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*QR코드 생성하기 버튼 클릭 이벤트*/
        binding.buttonMakeQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.makeQr(performance.getId());
            }
        });

        /*QR코드 생성하기 결과 감시*/
        viewModel.getIsMakeQrSuccess().observe(this, isMakeQrSuccess -> {
            Validation validation = isMakeQrSuccess.getValidation();
            Promote loadedQr = isMakeQrSuccess.getData();//TODO validation 바꿔야해
            if(validation == Validation.PERFORMANCE_GET_SUCCESS){
                promote = loadedQr;
                binding.buttonDownload.setEnabled(true);
                //TODO qr 이미지를 이미지뷰에 넣을 것
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "QR 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*다운로드 버튼 이벤트*/
        binding.buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.downloadQr(context, promote);
            }
        });
    }
}

package com.example.moum.view.moum;

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
import com.example.moum.data.entity.Settlement;
import com.example.moum.databinding.ActivityMoumPaymentAddBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.SettlementCreateDialog;
import com.example.moum.viewmodel.moum.MoumPaymentAddViewModel;

import java.util.ArrayList;

public class MoumPaymentAddActivity extends AppCompatActivity {
    private MoumPaymentAddViewModel viewModel;
    private ActivityMoumPaymentAddBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer moumId;
    private Integer teamId;
    private final ArrayList<Settlement> settlements = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumPaymentAddBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumPaymentAddViewModel.class);
        View view = binding.getRoot();
        binding.setViewModel(viewModel);
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

        /*제출 버튼 클릭 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.validCheck();
            }
        });

        /*valid check 감시 결과*/
        viewModel.getIsValidCheckSuccess().observe(this, isValidCheckSuccess -> {
            if (isValidCheckSuccess == Validation.VALID_ALL) {
                SettlementCreateDialog settlementCreateDialog = new SettlementCreateDialog(context, binding.edittextPaymentName.getText().toString());
                settlementCreateDialog.show();
            } else if (isValidCheckSuccess == Validation.SETTLEMENT_NAME_NOT_WRITTEN) {
                binding.errorPaymentName.setText("정산 내역 이름을 입력하세요.");
                binding.edittextPaymentName.requestFocus();
            } else if (isValidCheckSuccess == Validation.SETTLEMENT_FEE_NOT_WRITTEN) {
                binding.errorAmount.setText("정산 금액을 올바르게 입력하세요.");
                binding.edittextAmount.requestFocus();
            } else {
                Toast.makeText(context, "정산 내역 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*정산 내역 생성 결과 감시*/
        viewModel.getIsCreateSettlementsSuccess().observe(this, isCreateSettlementsSuccess -> {
            Validation validation = isCreateSettlementsSuccess.getValidation();
            Settlement settlement = isCreateSettlementsSuccess.getData();
            if (validation == Validation.SETTLEMENT_CREATE_SUCCESS) {
                Toast.makeText(context, "정산 내역을 생성하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else if (validation == Validation.NOT_VALID_ANYWAY) {
                Toast.makeText(context, "입력이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NO_AUTHORITY) {
                Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.MOUM_NOT_FOUND) {
                Toast.makeText(context, "모음을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "정산 내역 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*placeholder에 focus시 이벤트*/
        binding.edittextPaymentName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.errorPaymentName.setText("");
                    binding.placeholderPaymentName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                } else {
                    binding.placeholderPaymentName.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
        binding.edittextAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.errorAmount.setText("");
                    binding.placeholderAmount.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_mint_stroke));
                } else {
                    binding.placeholderAmount.setBackground(ContextCompat.getDrawable(context, R.drawable.background_rounded_gray_stroke));
                }
            }
        });
    }

    public void onSettlementCreateDialogYesClicked() {
        viewModel.createSettlement(moumId);
    }
}

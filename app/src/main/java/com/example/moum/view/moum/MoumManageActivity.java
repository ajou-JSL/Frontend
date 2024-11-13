package com.example.moum.view.moum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.databinding.ActivityMoumCreateBinding;
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.MoumCreateDialog;
import com.example.moum.view.dialog.MoumFinishDialog;
import com.example.moum.view.dialog.MoumReopenDialog;
import com.example.moum.view.moum.adapter.MoumCreateImageAdapter;
import com.example.moum.view.moum.adapter.MoumManageImageAdapter;
import com.example.moum.view.moum.adapter.MoumManageMemberAdapter;
import com.example.moum.viewmodel.moum.MoumCreateViewModel;
import com.example.moum.viewmodel.moum.MoumManageViewModel;

import java.util.ArrayList;

public class MoumManageActivity extends AppCompatActivity {
    private MoumManageViewModel viewModel;
    private ActivityMoumManageBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer id;
    private Integer moumId;
    private ArrayList<String> uris = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumManageBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumManageViewModel.class);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        moumId = prevIntent.getIntExtra("moumId", -1);

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
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

        /*설정 스피너 설정*/
        Spinner etcSpinner = binding.spinnerMoumManageEtc;
        String[] etcList = getResources().getStringArray(R.array.moum_manange_etc_list);
        ArrayAdapter<String> etcAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, etcList);
        etcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etcSpinner.setAdapter(etcAdapter);

        /*이미지 리사이클러뷰 연결*/
        RecyclerView imageRecyclerView = binding.recyclerMoumManageImage;
        MoumManageImageAdapter moumManageImageAdapter = new MoumManageImageAdapter();
        moumManageImageAdapter.setUris(uris, context);
        imageRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        imageRecyclerView.setAdapter(moumManageImageAdapter);

        /*멤버 리사이클러뷰 연결*/
        RecyclerView memberRecyclerView = binding.recyclerMoumManageMember;
        MoumManageMemberAdapter moumManageMemberAdapter = new MoumManageMemberAdapter();
        moumManageMemberAdapter.setMembers(members, context);
        memberRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        memberRecyclerView.setAdapter(moumManageMemberAdapter);

        /*모음 내용 조회하기*/
        viewModel.loadMoum(moumId);

        /*모음 내용 조회하기 결과 감시*/
        viewModel.getIsLoadMoumSuccess().observe(this, isLoadMoumSuccess -> {
            Validation validation = isLoadMoumSuccess.getValidation();
            Moum loadedMoum = isLoadMoumSuccess.getData();
            if(validation == Validation.GET_MOUM_SUCCESS){
               if(loadedMoum.getImageUrls() != null && !loadedMoum.getImageUrls().isEmpty()) {
                   uris.addAll(loadedMoum.getImageUrls());
                   moumManageImageAdapter.notifyItemInserted(uris.size() - 1);
               }
               members.addAll(loadedMoum.getMembers());
               moumManageMemberAdapter.notifyItemInserted(members.size()-1);
                if(loadedMoum.getPerformLocation() != null) binding.textviewMoumManagePlace.setText(loadedMoum.getPerformLocation());

                if(loadedMoum.getStartDate() != null && !loadedMoum.getStartDate().isEmpty() && loadedMoum.getEndDate() != null && !loadedMoum.getEndDate().isEmpty())
                    binding.textviewMoumManageDate.setText(String.format("%s ~ %s", loadedMoum.getStartDate(), loadedMoum.getEndDate()));
                else if(loadedMoum.getStartDate() != null && !loadedMoum.getStartDate().isEmpty() && (loadedMoum.getEndDate() == null || loadedMoum.getEndDate().isEmpty()))
                    binding.textviewMoumManageDate.setText(String.format("%s", loadedMoum.getStartDate()));
                else if(loadedMoum.getEndDate() != null && !loadedMoum.getEndDate().isEmpty() && (loadedMoum.getStartDate() == null || loadedMoum.getStartDate().isEmpty()))
                    binding.textviewMoumManageDate.setText(String.format("%s", loadedMoum.getEndDate()));
                else
                    binding.textviewMoumManageDate.setText("");
                if(loadedMoum.getPrice() != null) binding.textviewMoumManagePrice.setText(loadedMoum.getPrice().toString());
                if(loadedMoum.getMoumName() != null) binding.textviewMoumManageName.setText(loadedMoum.getMoumName());

                Moum.Process process = loadedMoum.getProcess();
                if(process.getFinishStatus()){
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonRecruit.setEnabled(false);
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                    binding.buttonFinish.setVisibility(View.GONE);
                    binding.buttonReopen.setVisibility(View.VISIBLE);
                }
                else{
                    if(process.getRecruitStatus()){
                        binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                        binding.buttonRecruit.setEnabled(false);
                    }
                    if(process.getChatroomStatus()){
                        binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                        binding.buttonMoumtalk.setEnabled(false);
                    }
                    if(process.getPracticeroomStatus()){
                        binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                        binding.buttonPracticeRoom.setEnabled(false);
                    }
                    if(process.getPerformLocationStatus()){
                        binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                        binding.buttonPerformLocation.setEnabled(false);
                    }
                    if(process.getPromoteStatus()){
                        binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                        binding.buttonPromote.setEnabled(false);
                    }
                    if(process.getPaymentStatus()){
                        binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                        binding.buttonPayment.setEnabled(false);
                    }
                }
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "모음 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*각 단계별 버튼 이벤트*/
        binding.buttonRecruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(context, "구인 게시판으로 이동", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonMoumtalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("fragment_index", 1); // chatroomFragment로 이동 (인덱스 1)
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        binding.buttonPracticeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(context, "연습실 찾기 페이지 이동", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonPerformLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(context, "공연장 찾기 페이지 이동", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(context, "공연 게시판으로 이동", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(context, "정산하기 페이지로 이동", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그 띄우기
                MoumFinishDialog moumFinishDialog = new MoumFinishDialog(context, binding.textviewMoumManageName.getText().toString());
                moumFinishDialog.show();
            }
        });
        binding.buttonReopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그 띄우기
                MoumReopenDialog moumReopenDialog = new MoumReopenDialog(context, binding.textviewMoumManageName.getText().toString());
                moumReopenDialog.show();
            }
        });

        /*각 단계별 설정 스피너 설정*/
        Spinner recruitSpinner = binding.spinnerMoumManageRecruit;
        Spinner moumtalkSpinner = binding.spinnerMoumManageMoumtalk;
        Spinner practiceRoomSpinner = binding.spinnerMoumManagePracticeRoom;
        Spinner performLocationSpinner = binding.spinnerMoumManagePerformLocation;
        Spinner promoteSpinner = binding.spinnerMoumManagePromote;
        Spinner paymentSpinner = binding.spinnerMoumManagePayment;
        String[] processList = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        ArrayAdapter<String> recruitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList);
        ArrayAdapter<String> moumtalkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList);
        ArrayAdapter<String> practiceRoomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList);
        ArrayAdapter<String> performLocationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList);
        ArrayAdapter<String> promoteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList);
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList);
        recruitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moumtalkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        practiceRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        performLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        promoteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recruitSpinner.setAdapter(recruitAdapter);
        moumtalkSpinner.setAdapter(moumtalkAdapter);
        practiceRoomSpinner.setAdapter(practiceRoomAdapter);
        performLocationSpinner.setAdapter(performLocationAdapter);
        promoteSpinner.setAdapter(promoteAdapter);
        paymentSpinner.setAdapter(paymentAdapter);

        /*모음 종료하기, 되살리기 감시 결과*/
        viewModel.getIsFinishMoumSuccess().observe(this, isFinishMoumSuccess -> {
            Validation validation = isFinishMoumSuccess.getValidation();
            Moum loadedMoum = isFinishMoumSuccess.getData();
            if(validation == Validation.FINISH_MOUM_SUCCESS){
                Toast.makeText(context, "모음을 마감하였습니다.", Toast.LENGTH_SHORT).show();
                binding.buttonFinish.setVisibility(View.GONE);
                binding.buttonReopen.setVisibility(View.VISIBLE);
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NO_AUTHORITY){
                Toast.makeText(context, "마감 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "모음 마감에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
        viewModel.getIsReopenMoumSuccess().observe(this, isReopenMoumSuccess -> {
            Validation validation = isReopenMoumSuccess.getValidation();
            Moum loadedMoum = isReopenMoumSuccess.getData();
            if(validation == Validation.REOPEN_MOUM_SUCCESS){
                Toast.makeText(context, "모음을 되살렸습니다.", Toast.LENGTH_SHORT).show();
                binding.buttonFinish.setVisibility(View.VISIBLE);
                binding.buttonReopen.setVisibility(View.GONE);
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NO_AUTHORITY){
                Toast.makeText(context, "마감 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "모음 마감에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
    }

    public void onMoumFinishDialogYesClicked(){
        viewModel.finishMoum(moumId);
    }

    public void onMoumReopenDialogYesClicked(){
        viewModel.reopenMoum(moumId);
    }


}

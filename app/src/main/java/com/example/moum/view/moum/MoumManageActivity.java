package com.example.moum.view.moum;

import static android.util.Log.e;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private Moum recentMoum;

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

        /*각 단계별 설정 스피너 설정*/
        Spinner recruitSpinner = binding.spinnerMoumManageRecruit;
        Spinner moumtalkSpinner = binding.spinnerMoumManageMoumtalk;
        Spinner practiceRoomSpinner = binding.spinnerMoumManagePracticeRoom;
        Spinner performLocationSpinner = binding.spinnerMoumManagePerformLocation;
        Spinner promoteSpinner = binding.spinnerMoumManagePromote;
        Spinner paymentSpinner = binding.spinnerMoumManagePayment;
        String[] processList1 = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        String[] processList2 = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        String[] processList3 = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        String[] processList4 = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        String[] processList5 = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        String[] processList6 = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        ArrayAdapter<String> recruitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList1);
        ArrayAdapter<String> moumtalkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList2);
        ArrayAdapter<String> practiceRoomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList3);
        ArrayAdapter<String> performLocationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList4);
        ArrayAdapter<String> promoteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList5);
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processList6);
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
//        recruitSpinner.setSelection(0, true);
//        moumtalkSpinner.setSelection(0, true);
//        practiceRoomSpinner.setSelection(0, true);
//        performLocationSpinner.setSelection(0, true);
//        promoteSpinner.setSelection(0, true);
//        paymentSpinner.setSelection(0, true);

        /*각 단계별 설정 스피너 이벤트*/
        recruitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "아이템 선택됨");
                if(position == 0){
                    Log.e(TAG, "스피너 눌림");
                    if(recentMoum == null || recentMoum.getProcess() == null) return;
                    Log.e(TAG, "모음 비어있지 않음: " + recentMoum.getProcess().getRecruitStatus());
                    Moum.Process process = recentMoum.getProcess();
                    if(process.getRecruitStatus()){
                        Log.e(TAG, "상태 true");
                        process.setRecruitStatus(false);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                    else{
                        Log.e(TAG, "상태 false");
                        process.setRecruitStatus(true);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                }
                else{
                    e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        moumtalkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(recentMoum == null || recentMoum.getProcess() == null) return;
                    Moum.Process process = recentMoum.getProcess();
                    if(process.getChatroomStatus()){
                        process.setChatroomStatus(false);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                    else{
                        process.setChatroomStatus(true);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                }
                else{
                    e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        practiceRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(recentMoum == null || recentMoum.getProcess() == null) return;
                    Moum.Process process = recentMoum.getProcess();
                    if(process.getPracticeroomStatus()){
                        process.setPracticeroomStatus(false);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                    else{
                        process.setPracticeroomStatus(true);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                }
                else{
                    e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        performLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(recentMoum == null || recentMoum.getProcess() == null) return;
                    Moum.Process process = recentMoum.getProcess();
                    if(process.getPerformLocationStatus()){
                        process.setPerformLocationStatus(false);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                    else{
                        process.setPerformLocationStatus(true);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                }
                else{
                    e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        promoteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(recentMoum == null || recentMoum.getProcess() == null) return;
                    Moum.Process process = recentMoum.getProcess();
                    if(process.getPromoteStatus()){
                        process.setPromoteStatus(false);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                    else{
                        process.setPromoteStatus(true);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                }
                else{
                    e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    if(recentMoum == null || recentMoum.getProcess() == null) return;
                    Moum.Process process = recentMoum.getProcess();
                    if(process.getPaymentStatus()){
                        process.setPaymentStatus(false);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                    else{
                        process.setPaymentStatus(true);
                        viewModel.updateProcessMoum(moumId, process);
                    }
                }
                else{
                    e(TAG, "알 수 없는 아이템 선택");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*모음 진척도 업데이트 감시 결과*/
        viewModel.getIsUpdateMoumSuccess().observe(this, isUpdateMoumSuccess -> {
            Validation validation = isUpdateMoumSuccess.getValidation();
            Moum.Process updatedProcess = isUpdateMoumSuccess.getData().getProcess();
            recentMoum = isUpdateMoumSuccess.getData();
            if(validation == Validation.UPDATE_PROCESS_MOUM_SUCCESS){
                Toast.makeText(context, "진척도를 업데이트하였습니다.", Toast.LENGTH_SHORT).show();
                if(updatedProcess.getRecruitStatus()){
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonRecruit.setEnabled(false);
                }
                else{
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonRecruit.setEnabled(true);
                }
                if(updatedProcess.getChatroomStatus()){
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                }
                else{
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonMoumtalk.setEnabled(true);
                }
                if(updatedProcess.getPracticeroomStatus()){
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                }
                else{
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPracticeRoom.setEnabled(true);
                }
                if(updatedProcess.getPerformLocationStatus()){
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                }
                else{
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPerformLocation.setEnabled(true);
                }
                if(updatedProcess.getPromoteStatus()){
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                }
                else{
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPromote.setEnabled(true);
                }
                if(updatedProcess.getPaymentStatus()){
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                }
                else{
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPayment.setEnabled(true);
                }
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.ILLEGAL_ARGUMENT){
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NO_AUTHORITY){
                Toast.makeText(context, "업데이트 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "진척도 업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*모음 내용 조회하기*/
        viewModel.loadMoum(moumId);

        /*모음 내용 조회하기 결과 감시*/
        viewModel.getIsLoadMoumSuccess().observe(this, isLoadMoumSuccess -> {
            Validation validation = isLoadMoumSuccess.getValidation();
            Moum loadedMoum = isLoadMoumSuccess.getData();
            recentMoum = loadedMoum;
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
                    recruitSpinner.setEnabled(false);
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                    moumtalkSpinner.setEnabled(false);
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                    practiceRoomSpinner.setEnabled(false);
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                    performLocationSpinner.setEnabled(false);
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                    promoteSpinner.setEnabled(false);
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                    paymentSpinner.setEnabled(false);
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
                e(TAG, "감시 결과를 알 수 없습니다.");
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

        /*모음 종료하기, 되살리기 감시 결과*/
        viewModel.getIsFinishMoumSuccess().observe(this, isFinishMoumSuccess -> {
            Validation validation = isFinishMoumSuccess.getValidation();
            Moum loadedMoum = isFinishMoumSuccess.getData();
            recentMoum = loadedMoum;
            if(validation == Validation.FINISH_MOUM_SUCCESS){
                Toast.makeText(context, "모음을 마감하였습니다.", Toast.LENGTH_SHORT).show();
                binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonRecruit.setEnabled(false);
                recruitSpinner.setEnabled(false);
                binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonMoumtalk.setEnabled(false);
                moumtalkSpinner.setEnabled(false);
                binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPracticeRoom.setEnabled(false);
                practiceRoomSpinner.setEnabled(false);
                binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPerformLocation.setEnabled(false);
                performLocationSpinner.setEnabled(false);
                binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPromote.setEnabled(false);
                promoteSpinner.setEnabled(false);
                binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPayment.setEnabled(false);
                paymentSpinner.setEnabled(false);
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
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
        viewModel.getIsReopenMoumSuccess().observe(this, isReopenMoumSuccess -> {
            Validation validation = isReopenMoumSuccess.getValidation();
            Moum.Process updatedProcess = isReopenMoumSuccess.getData().getProcess();
            recentMoum = isReopenMoumSuccess.getData();
            if(validation == Validation.REOPEN_MOUM_SUCCESS){
                Toast.makeText(context, "모음을 되살렸습니다.", Toast.LENGTH_SHORT).show();
                recruitSpinner.setEnabled(true);
                moumtalkSpinner.setEnabled(true);
                practiceRoomSpinner.setEnabled(true);
                performLocationSpinner.setEnabled(true);
                promoteSpinner.setEnabled(true);
                paymentSpinner.setEnabled(true);
                if(updatedProcess.getRecruitStatus()){
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonRecruit.setEnabled(false);
                }
                else{
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonRecruit.setEnabled(true);
                }
                if(updatedProcess.getChatroomStatus()){
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                }
                else{
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonMoumtalk.setEnabled(true);
                }
                if(updatedProcess.getPracticeroomStatus()){
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                }
                else{
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPracticeRoom.setEnabled(true);
                }
                if(updatedProcess.getPerformLocationStatus()){
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                }
                else{
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPerformLocation.setEnabled(true);
                }
                if(updatedProcess.getPromoteStatus()){
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                }
                else{
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPromote.setEnabled(true);
                }
                if(updatedProcess.getPaymentStatus()){
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                }
                else{
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_neon_mint_angular_ripple));
                    binding.buttonPayment.setEnabled(true);
                }
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
                e(TAG, "감시 결과를 알 수 없습니다.");
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

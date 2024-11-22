package com.example.moum.view.moum;

import static android.util.Log.e;
import static android.util.Log.i;
import static android.util.Log.v;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.moum.data.entity.Music;
import com.example.moum.databinding.ActivityMoumCreateBinding;
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.MoumCreateDialog;
import com.example.moum.view.dialog.MoumDeleteDialog;
import com.example.moum.view.dialog.MoumFinishDialog;
import com.example.moum.view.dialog.MoumReopenDialog;
import com.example.moum.view.moum.adapter.MoumCreateImageAdapter;
import com.example.moum.view.moum.adapter.MoumManageImageAdapter;
import com.example.moum.view.moum.adapter.MoumManageMemberAdapter;
import com.example.moum.view.moum.adapter.MoumManageMusicAdapter;
import com.example.moum.viewmodel.moum.MoumCreateViewModel;
import com.example.moum.viewmodel.moum.MoumManageViewModel;

import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<Music> musics = new ArrayList<>();
    private Moum recentMoum;
    private boolean isSpinnerInitialized = false;
    private static final int REQUEST_CODE = 200;

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
        if(moumId == -1){
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

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

        /*설정 드롭다운 설정*/
        ImageView dropdownMenu = binding.dropdownMenu;
        String[] etcList = getResources().getStringArray(R.array.moum_manange_etc_list);
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, dropdownMenu);
                for (int i = 0; i < etcList.length; i++) {
                    popupMenu.getMenu().add(etcList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("수정하기")) {
                            Intent intent = new Intent(MoumManageActivity.this, MoumUpdateActivity.class);
                            intent.putExtra("moumId", moumId);
                            intent.putExtra("teamId", recentMoum.getTeamId());
                            intent.putExtra("leaderId", recentMoum.getLeaderId());
                            startActivity(intent);
                        } else if (selectedItem.equals("삭제하기")) {
                            MoumDeleteDialog moumDeleteDialog = new MoumDeleteDialog(context, recentMoum.getMoumName());
                            moumDeleteDialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

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

        /*곡 리사이클러뷰 연결*/
        RecyclerView musicRecyclerView = binding.recyclerMoumManageMusic;
        MoumManageMusicAdapter moumManageMusicAdapter = new MoumManageMusicAdapter();
        moumManageMusicAdapter.setMusics(musics, context);
        musicRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        musicRecyclerView.setAdapter(moumManageMusicAdapter);

        /*각 단계별 설정 드롭다운 설정*/
        String[] processList = getResources().getStringArray(R.array.moum_manange_process_etc_list);
        binding.dropdownRecruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, binding.dropdownRecruit);
                for (int i = 0; i < processList.length; i++) {
                    popupMenu.getMenu().add(processList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("업데이트 상태 토글하기")) {
                            if(recentMoum == null || recentMoum.getProcess() == null) return true;
                            Moum.Process process = recentMoum.getProcess();
                            if(process.getRecruitStatus()){
                                process.setRecruitStatus(false);
                                viewModel.updateProcessMoum(moumId, process);
                            }
                            else{
                                process.setRecruitStatus(true);
                                viewModel.updateProcessMoum(moumId, process);
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        binding.dropdownMoumtalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, binding.dropdownMoumtalk);
                for (int i = 0; i < processList.length; i++) {
                    popupMenu.getMenu().add(processList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("업데이트 상태 토글하기")) {
                            if(recentMoum == null || recentMoum.getProcess() == null) return true;
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
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        binding.dropdownPracticeroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, binding.dropdownPracticeroom);
                for (int i = 0; i < processList.length; i++) {
                    popupMenu.getMenu().add(processList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("업데이트 상태 토글하기")) {
                            if(recentMoum == null || recentMoum.getProcess() == null) return true;
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
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        binding.dropdownPerformLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, binding.dropdownPerformLocation);
                for (int i = 0; i < processList.length; i++) {
                    popupMenu.getMenu().add(processList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("업데이트 상태 토글하기")) {
                            if(recentMoum == null || recentMoum.getProcess() == null) return true;
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
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        binding.dropdownPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, binding.dropdownPromote);
                for (int i = 0; i < processList.length; i++) {
                    popupMenu.getMenu().add(processList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("업데이트 상태 토글하기")) {
                            if(recentMoum == null || recentMoum.getProcess() == null) return true;
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
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        binding.dropdownPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoumManageActivity.this, binding.dropdownPayment);
                for (int i = 0; i < processList.length; i++) {
                    popupMenu.getMenu().add(processList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("업데이트 상태 토글하기")) {
                            if(recentMoum == null || recentMoum.getProcess() == null) return true;
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
                        return true;
                    }
                });
                popupMenu.show();
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
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_0_ripple));
                    binding.buttonRecruit.setEnabled(true);
                }
                if(updatedProcess.getChatroomStatus()){
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                }
                else{
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_1_ripple));
                    binding.buttonMoumtalk.setEnabled(true);
                }
                if(updatedProcess.getPracticeroomStatus()){
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                }
                else{
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_2_ripple));
                    binding.buttonPracticeRoom.setEnabled(true);
                }
                if(updatedProcess.getPerformLocationStatus()){
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                }
                else{
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_3_ripple));
                    binding.buttonPerformLocation.setEnabled(true);
                }
                if(updatedProcess.getPromoteStatus()){
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                }
                else{
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_4_ripple));
                    binding.buttonPromote.setEnabled(true);
                }
                if(updatedProcess.getPaymentStatus()){
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                }
                else{
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_5_ripple));
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
               musics.addAll(loadedMoum.getMusic());
               moumManageMusicAdapter.notifyItemInserted(musics.size()-1);
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
                    binding.dropdownRecruit.setClickable(false);
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                    binding.dropdownMoumtalk.setClickable(false);
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                    binding.dropdownPracticeroom.setClickable(false);
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                    binding.dropdownPerformLocation.setClickable(false);
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                    binding.dropdownPromote.setClickable(false);
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                    binding.dropdownPayment.setClickable(false);
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
                Intent intent = new Intent(MoumManageActivity.this, MoumFindPracticeroomActivity.class);
                intent.putExtra("teamId", recentMoum.getTeamId());
                intent.putExtra("moumId", recentMoum.getMoumId());
                intent.putExtra("leaderId", recentMoum.getLeaderId());
                startActivity(intent);

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
                Intent intent = new Intent(MoumManageActivity.this, MoumPromote1Activity.class);
                intent.putExtra("moumId", recentMoum.getMoumId());
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        binding.buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoumManageActivity.this, MoumPaymentActivity.class);
                intent.putExtra("teamId", recentMoum.getTeamId());
                intent.putExtra("moumId", recentMoum.getMoumId());
                startActivity(intent);
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
                binding.dropdownRecruit.setClickable(false);
                binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonMoumtalk.setEnabled(false);
                binding.dropdownMoumtalk.setClickable(false);
                binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPracticeRoom.setEnabled(false);
                binding.dropdownPracticeroom.setClickable(false);
                binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPerformLocation.setEnabled(false);
                binding.dropdownPerformLocation.setClickable(false);
                binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPromote.setEnabled(false);
                binding.dropdownPromote.setClickable(false);
                binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                binding.buttonPayment.setEnabled(false);
                binding.dropdownPayment.setClickable(false);
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
                binding.dropdownRecruit.setClickable(true);
                binding.dropdownMoumtalk.setClickable(true);
                binding.dropdownPracticeroom.setClickable(true);
                binding.dropdownPerformLocation.setClickable(true);
                binding.dropdownPromote.setClickable(true);
                binding.dropdownPayment.setClickable(true);
                if(updatedProcess.getRecruitStatus()){
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonRecruit.setEnabled(false);
                }
                else{
                    binding.buttonRecruit.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_0_ripple));
                    binding.buttonRecruit.setEnabled(true);
                }
                if(updatedProcess.getChatroomStatus()){
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonMoumtalk.setEnabled(false);
                }
                else{
                    binding.buttonMoumtalk.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_1_ripple));
                    binding.buttonMoumtalk.setEnabled(true);
                }
                if(updatedProcess.getPracticeroomStatus()){
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPracticeRoom.setEnabled(false);
                }
                else{
                    binding.buttonPracticeRoom.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_2_ripple));
                    binding.buttonPracticeRoom.setEnabled(true);
                }
                if(updatedProcess.getPerformLocationStatus()){
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPerformLocation.setEnabled(false);
                }
                else{
                    binding.buttonPerformLocation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_3_ripple));
                    binding.buttonPerformLocation.setEnabled(true);
                }
                if(updatedProcess.getPromoteStatus()){
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPromote.setEnabled(false);
                }
                else{
                    binding.buttonPromote.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_4_ripple));
                    binding.buttonPromote.setEnabled(true);
                }
                if(updatedProcess.getPaymentStatus()){
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_gray_ripple));
                    binding.buttonPayment.setEnabled(false);
                }
                else{
                    binding.buttonPayment.setBackground(ContextCompat.getDrawable(context, R.drawable.button_mint_pink_5_ripple));
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

        /*삭제 결과 감시*/
        viewModel.getIsDeleteMoumSuccess().observe(this, isDeleteMoumSuccess -> {
            Validation validation = isDeleteMoumSuccess.getValidation();
            Moum deletedMoum = isDeleteMoumSuccess.getData();
            if(validation == Validation.DELETE_MOUM_SUCCESS){
                Toast.makeText(context, "'" + deletedMoum.getMoumName() + "'\n모음을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
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
                Toast.makeText(context, "모음 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*activity로 리턴 시, result와 함께 리턴된다면 activity 종료*/
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        if (resultCode == RESULT_OK && data != null) {
            Log.e(TAG, "onActivityResult RESULT_OK!");
            int fragmentIndex = data.getIntExtra("fragment_index", -1);
            int finish = data.getIntExtra("finish", -1);
            Log.e(TAG, "finish: " + finish);
            if(finish == 1){
                Intent intent = new Intent();
                intent.putExtra("fragment_index", fragmentIndex);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void onMoumFinishDialogYesClicked(){
        viewModel.finishMoum(moumId);
    }

    public void onMoumReopenDialogYesClicked(){
        viewModel.reopenMoum(moumId);
    }

    public void onMoumDeleteDialogYesClicked() { viewModel.deleteMoum(moumId); }

}

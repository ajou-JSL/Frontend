package com.example.moum.view.moum;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Music;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.databinding.ActivityMoumFindPracticeroomBinding;
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.utils.PermissionManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.moum.adapter.MoumPracticeroomAdapter;
import com.example.moum.view.moum.adapter.MoumUpdateImageAdapter;
import com.example.moum.viewmodel.moum.MoumFindPracticeroomViewModel;
import com.example.moum.viewmodel.moum.MoumManageViewModel;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MoumFindPracticeroomActivity extends AppCompatActivity {
    private MoumFindPracticeroomViewModel viewModel;
    private ActivityMoumFindPracticeroomBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private PermissionManager permissionManager;
    private LatLng latLng;
    private ArrayList<Practiceroom> practicerooms = new ArrayList<>();
    private Integer memberId;
    private Integer teamId;
    private Integer moumId;
    private Integer leaderId;
    private Boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumFindPracticeroomBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumFindPracticeroomViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        // GPS 관련 권한 체크
        permissionCheck();

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        teamId = prevIntent.getIntExtra("teamId", -1);
        moumId = prevIntent.getIntExtra("moumId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        if (teamId == -1 || moumId == -1 || leaderId == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
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

        /*연습실 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerPracticeroom;
        MoumPracticeroomAdapter moumPracticeroomAdapter = new MoumPracticeroomAdapter();
        moumPracticeroomAdapter.setPracticerooms(practicerooms, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(moumPracticeroomAdapter);

        /*연습실 불러오기*/
        viewModel.loadPracticeroom();

        /*연습실 불러오기 결과 감시*/
        viewModel.getIsLoadPracticeroomSuccess().observe(this, isLoadPracticeroomSuccess -> {
            Validation validation = isLoadPracticeroomSuccess.getValidation();
            List<Practiceroom> loadedPracticerooms = isLoadPracticeroomSuccess.getData();
            if (validation == Validation.PRACTICE_ROOM_GET_SUCCESS && !loadedPracticerooms.isEmpty()) {
                practicerooms.clear();
                practicerooms.addAll(loadedPracticerooms);
                moumPracticeroomAdapter.notifyItemInserted(practicerooms.size() - 1);
                viewModel.setRecentPageNumber(loadedPracticerooms.size());
            } else if (validation == Validation.PRACTICE_ROOM_GET_SUCCESS) {
                Toast.makeText(context, "등록된 연습실이 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "연습실 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*최하단에서 스크롤 시 다음 리스트 불러오기*/
        long DEBOUNCE_DELAY = 0;
        Handler handler = new Handler(Looper.getMainLooper());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                    if (practicerooms.isEmpty())
                        return;
                    isLoading = true;
                    viewModel.loadNextPracticeroom();
                    handler.postDelayed(() -> isLoading = false, DEBOUNCE_DELAY);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        /*다음 연습실 리스트 가져오기 감시 결과*/
        viewModel.getIsLoadNextPracticeroomSuccess().observe(this, isLoadNextPracticeroomSuccess -> {
            Validation validation = isLoadNextPracticeroomSuccess.getValidation();
            List<Practiceroom> loadedPracticerooms = isLoadNextPracticeroomSuccess.getData();
            if (validation == Validation.PRACTICE_ROOM_GET_SUCCESS && !loadedPracticerooms.isEmpty()) {
                practicerooms.addAll(loadedPracticerooms);
                moumPracticeroomAdapter.notifyItemInserted(practicerooms.size() - 1);
                viewModel.setRecentPageNumber(loadedPracticerooms.size());
            } else if (validation == Validation.PRACTICE_ROOM_GET_SUCCESS) {
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "다음 연습실 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });

        /*검색창 세팅*/
        binding.searchviewPracticeroom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.queryPracticeroom(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        /*검색 결과 감시*/
        viewModel.getIsQueryPracticeroomSuccess().observe(this, isQueryPracticeroomSuccess -> {
            Validation validation = isQueryPracticeroomSuccess.getValidation();
            List<Practiceroom> loadedPracticerooms = isQueryPracticeroomSuccess.getData();
            if (validation == Validation.PRACTICE_ROOM_GET_SUCCESS) { //TODO 바꿔야해
                practicerooms.clear();
                practicerooms.addAll(loadedPracticerooms);
                moumPracticeroomAdapter.notifyDataSetChanged();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "검색에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
    }

    public void onPracticeroomClicked(Integer practiceroomId) {
        Intent intent = new Intent(MoumFindPracticeroomActivity.this, MoumMapPracticeroomActivity.class);
        intent.putExtra("practiceroomId", practiceroomId);
        intent.putExtra("teamId", teamId);
        intent.putExtra("moumId", moumId);
        intent.putExtra("leaderId", leaderId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clearPage();
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            permissionManager = new PermissionManager(this, this);
            if (!permissionManager.checkPermission()) {
                permissionManager.requestPermission();
            } else {
                latLng = getCurrentLatLng();
                Log.e(TAG, latLng.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!permissionManager.permissionResult(requestCode, permissions, grantResults)) {
            permissionManager.requestPermission();
        } else {
            latLng = getCurrentLatLng();
            Log.e(TAG, latLng.toString());
        }
    }

    private LatLng getCurrentLatLng() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation != null) {
            return new LatLng(currentLocation);
        }
        else{
            // 기본 설정 위치
            Log.e(TAG, "기본 위치로 설정되었습니다.");
            return new LatLng(37.279784, 127.043664);
        }
    }
}

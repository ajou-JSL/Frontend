package com.example.moum.view.moum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.MoumPerformHall;
import com.example.moum.data.entity.MoumPracticeroom;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.databinding.ActivityMoumListPerformancehallBinding;
import com.example.moum.databinding.ActivityMoumMapPerformancehallBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.moum.adapter.PerformOfMoumAdapter;
import com.example.moum.viewmodel.moum.MoumListPerformanceHallViewModel;
import com.example.moum.viewmodel.moum.MoumMapPerformanceHallViewModel;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MoumListPerformanceHallActivity extends AppCompatActivity {
    private MoumListPerformanceHallViewModel viewModel;
    private ActivityMoumListPerformancehallBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer memberId;
    private Integer teamId;
    private Integer moumId;
    private Integer leaderId;
    private NaverMap naverMap;
    private ArrayList<MoumPerformHall> performanceHallsOfMoum = new ArrayList<>();
    private ArrayList<PerformanceHall> performanceHalls = new ArrayList<>();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumListPerformancehallBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumListPerformanceHallViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        teamId = prevIntent.getIntExtra("teamId", -1);
        moumId = prevIntent.getIntExtra("moumId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        if(teamId == -1 || moumId == -1 || leaderId == -1){
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
//            finish();
        }

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
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

        /*모음의 공연장 스피너 연결*/
        RecyclerView recyclerView = binding.recyclerPerformanceHallOfMoum;
        PerformOfMoumAdapter performOfMoumAdapter = new PerformOfMoumAdapter();
        performOfMoumAdapter.setPerformanceHalls(performanceHalls, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setAdapter(performOfMoumAdapter);

        /*모음의 공연장 정보 불러오기*/
        viewModel.loadPerformanceHallsOfMoum(moumId);

        /*모음의 공연장 정보 불러오기 결과 감시*/
        viewModel.getIsLoadPerformanceHallsOfMoumSuccess().observe(this, isLoadPerformanceHallsOfMoumSuccess -> {
            Validation validation = isLoadPerformanceHallsOfMoumSuccess.getValidation();
            List<MoumPerformHall> loadedPerformanceHalls = isLoadPerformanceHallsOfMoumSuccess.getData();
            if(validation == Validation.MOUM_PERFORMANCE_HALL_GET_SUCCESS){
                performanceHallsOfMoum.clear();
                performanceHallsOfMoum.addAll(loadedPerformanceHalls);
                performanceHalls.clear();
                performanceHalls.add(0, new PerformanceHall());
                for(MoumPerformHall moumPerformHall : loadedPerformanceHalls){
                    if(moumPerformHall.getHallId() != null)
                        viewModel.loadPerformanceHall(moumPerformHall.getHallId());
                    else{
                        PerformanceHall performanceHall = new PerformanceHall();
                        performanceHall.setName(moumPerformHall.getHallName());
                        performanceHalls.add(performanceHall);
                    }
                    performOfMoumAdapter.notifyItemInserted(performanceHalls.size()-1);
                }
            }
            else if(validation == Validation.MOUM_PERFORMANCE_HALL_NOT_FOUND){
                performanceHalls.clear();
                performanceHalls.add(null);
                performOfMoumAdapter.notifyItemInserted(0);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "공연장 리스트를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });

        /*공연장 상세정보 불러오기 결과 감시*/
        viewModel.getIsLoadPerformanceHallSuccess().observe(this, isLoadPerformanceHallSuccess -> {
            Validation validation = isLoadPerformanceHallSuccess.getValidation();
            PerformanceHall performanceHall = isLoadPerformanceHallSuccess.getData();
            if(validation == Validation.PERFORMANCE_HALL_GET_SUCCESS){
                performanceHalls.add(0, performanceHall);
                performOfMoumAdapter.notifyItemInserted(performanceHalls.size()-1);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "공연장 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });
    }


    public void onPerformanceHallClicked(Integer performanceHallId){
        Intent intent = new Intent(MoumListPerformanceHallActivity.this, MoumMapPerformanceHallActivity.class);
        intent.putExtra("performanceHallId", performanceHallId);
        intent.putExtra("moumId", moumId);
        intent.putExtra("teamId", teamId);
        intent.putExtra("leaderId", leaderId);
        startActivity(intent);
    }

    public void onPerformFindButtonClicked(){
        Intent intent = new Intent(MoumListPerformanceHallActivity.this, MoumFindPerformanceHallActivity.class);
        intent.putExtra("moumId", moumId);
        intent.putExtra("teamId", teamId);
        intent.putExtra("leaderId", leaderId);
        startActivity(intent);
    }
}

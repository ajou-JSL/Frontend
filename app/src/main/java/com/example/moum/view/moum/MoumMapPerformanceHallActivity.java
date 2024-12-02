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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.MoumPerformHall;
import com.example.moum.data.entity.PerformanceHall;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.databinding.ActivityMoumMapPerformancehallBinding;
import com.example.moum.databinding.ActivityMoumMapPracticeroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.dialog.PerformOfMoumCreateDialog;
import com.example.moum.viewmodel.moum.MoumMapPerformanceHallViewModel;
import com.example.moum.viewmodel.moum.MoumMapPracticeroomViewModel;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;

public class MoumMapPerformanceHallActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MoumMapPerformanceHallViewModel viewModel;
    private ActivityMoumMapPerformancehallBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer performanceHallId;
    private Integer memberId;
    private Integer teamId;
    private Integer moumId;
    private Integer leaderId;
    private NaverMap naverMap;
    private PerformanceHall performanceHall;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumMapPerformancehallBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumMapPerformanceHallViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        performanceHallId = prevIntent.getIntExtra("performanceHallId", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        moumId = prevIntent.getIntExtra("moumId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        if(performanceHallId == -1 || teamId == -1 || moumId == -1 || leaderId == -1){
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

        /*네이버 지도 연동한 지도뷰 설정*/
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        /*연습실 정보 불러오기*/
        viewModel.loadPerformanceHall(performanceHallId);

        /*연습실 정보 불러오기 결과 감시*/
        binding.buttonAddInMoum.setEnabled(false);
        viewModel.getIsLoadPerformanceHallSuccess().observe(this, isLoadPerformanceHallSuccess -> {
            Validation validation = isLoadPerformanceHallSuccess.getValidation();
            PerformanceHall loadedPerformanceHall = isLoadPerformanceHallSuccess.getData();
            if(validation == Validation.PERFORMANCE_HALL_GET_SUCCESS){
                performanceHall = loadedPerformanceHall;
                if(loadedPerformanceHall.getName() != null) binding.textviewPerformanceHallName.setText(loadedPerformanceHall.getName());
                if(loadedPerformanceHall.getPrice() != null) binding.textviewPerformanceHallPrice.setText(String.format("%d", loadedPerformanceHall.getPrice()));
                if(loadedPerformanceHall.getCapacity() != null) binding.textviewPerformanceHallCapacity.setText(String.format("%d", loadedPerformanceHall.getCapacity()));
                if(loadedPerformanceHall.getSize() != null) binding.textviewPerformanceHallSize.setText(String.format("%d", loadedPerformanceHall.getSize()));
                if(loadedPerformanceHall.getAddress() != null) binding.textviewPerformanceHallAddress.setText(loadedPerformanceHall.getAddress());
                if(loadedPerformanceHall.getOwner() != null) binding.textviewPerformanceHallOwner.setText(loadedPerformanceHall.getOwner());
                if(loadedPerformanceHall.getPhone() != null) binding.textviewPerformanceHallPhone.setText(loadedPerformanceHall.getPhone());
                if(loadedPerformanceHall.getEmail() != null) binding.textviewPerformanceHallEmail.setText(loadedPerformanceHall.getEmail());
                if(loadedPerformanceHall.getStand() != null) binding.textviewPerformanceHallStand.setText(String.format("%d", loadedPerformanceHall.getStand()));
                if(loadedPerformanceHall.getHasPiano() != null && loadedPerformanceHall.getHasPiano()) {
                    binding.checkboxPerformanceHallPiano.setText("있음");
                    binding.checkboxPerformanceHallPiano.setChecked(true);
                    binding.checkboxPerformanceHallPiano.setEnabled(false);
                }
                if(loadedPerformanceHall.getHasAmp() != null && loadedPerformanceHall.getHasAmp()) {
                    binding.checkboxPerformanceHallAmp.setText("있음");
                    binding.checkboxPerformanceHallAmp.setChecked(true);
                    binding.checkboxPerformanceHallAmp.setEnabled(false);
                }
                if(loadedPerformanceHall.getHasSpeaker() != null && loadedPerformanceHall.getHasSpeaker()) {
                    binding.checkboxPerformanceHallSpeaker.setText("있음");
                    binding.checkboxPerformanceHallSpeaker.setChecked(true);
                    binding.checkboxPerformanceHallSpeaker.setEnabled(false);
                }
                if(loadedPerformanceHall.getHasDrums() != null && loadedPerformanceHall.getHasDrums()) {
                    binding.checkboxPerformanceHallDrum.setText("있음");
                    binding.checkboxPerformanceHallDrum.setChecked(true);
                    binding.checkboxPerformanceHallDrum.setEnabled(false);
                }
                if(loadedPerformanceHall.getImageUrls() != null && !loadedPerformanceHall.getImageUrls().isEmpty()) {
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_more_rounded_gray_size_fit2)
                            .error(R.drawable.background_more_rounded_gray_size_fit2))
                            .load(loadedPerformanceHall.getImageUrls().get(0)).into(binding.imageviewPerformanceHall);
                    binding.imageviewPerformanceHall.setClipToOutline(true);
                }
                if(loadedPerformanceHall.getLatitude() != null && loadedPerformanceHall.getLongitude() != null) {
                    if (Boolean.TRUE.equals(viewModel.getIsNaverMapReady().getValue())) {
                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(loadedPerformanceHall.getLatitude(), loadedPerformanceHall.getLongitude()));
                        marker.setMap(naverMap);
                        marker.setCaptionText(loadedPerformanceHall.getName());
                        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(loadedPerformanceHall.getLatitude(), loadedPerformanceHall.getLongitude()));
                        naverMap.moveCamera(cameraUpdate);
                    }
                }
                if(loadedPerformanceHall.getMapUrl() != null)
                    binding.buttonGotoNaverMap.setEnabled(true);
                else
                    binding.buttonGotoNaverMap.setEnabled(false);
                binding.buttonAddInMoum.setEnabled(true);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "연습실 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });

        /*네이버 지도에서 보기 버튼 클릭*/
        binding.buttonGotoNaverMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(performanceHall != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(performanceHall.getMapUrl()));
                    startActivity(intent);
                }
            }
        });

        /*모음에 등록하기 버튼 클릭*/
        binding.buttonAddInMoum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!memberId.equals(leaderId)){
                    Toast.makeText(MoumMapPerformanceHallActivity.this, "리더만 등록할 수 있어요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                PerformOfMoumCreateDialog performOfMoumCreateDialog = new PerformOfMoumCreateDialog(context, performanceHall.getName());
                performOfMoumCreateDialog.show();
            }
        });

        /*모음에 등록하기 결과 감시*/
        viewModel.getIsCreatePerformanceHallSuccess().observe(this, isCreatePerformanceHallSuccess -> {
            Validation validation = isCreatePerformanceHallSuccess.getValidation();
            MoumPerformHall createdPerformanceHall = isCreatePerformanceHallSuccess.getData();
            if(validation == Validation.MOUM_PERFORMANCE_HALL_CREATE_SUCCESS){
                Toast.makeText(context, "모음에 등록을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(validation == Validation.MOUM_NOT_FOUND){
                Toast.makeText(context, "존재하지 않는 모음입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "모음 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });

        /*map ready시 결과 감시*/

//        viewModel.getIsNaverMapReady().observe(this, isNaverMapReady -> {
//            if(isNaverMapReady && performanceHall != null && naverMap != null){
//                Marker marker = new Marker();
//                marker.setPosition(new LatLng(performanceHall.getLatitude(), performanceHall.getLongitude()));
//                marker.setMap(naverMap);
//                marker.setCaptionText(performanceHall.getName());
//                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(performanceHall.getLatitude(), performanceHall.getLongitude()));
//                naverMap.moveCamera(cameraUpdate);
//            }
//        });

        /*map도 준비되고, 공연장 정보도 준비되었을 시 결과 감시*/
        viewModel.getIsAllReady().observe(this, isAllReady -> {
            if(isAllReady){
                Marker marker = new Marker();
                marker.setPosition(new LatLng(performanceHall.getLatitude(), performanceHall.getLongitude()));
                marker.setMap(naverMap);
                marker.setCaptionText(performanceHall.getName());
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(performanceHall.getLatitude(), performanceHall.getLongitude()));
                naverMap.moveCamera(cameraUpdate);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        performanceHall = null;
        naverMap = null;
        viewModel.setIsNaverMapReady(false);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);
        this.naverMap = naverMap;
        viewModel.setIsNaverMapReady(true);
    }

    public void onPerformOfMoumCreateDialogYesClicked(){
        if(performanceHall != null)
            viewModel.createPerformanceHall(moumId, performanceHall.getId());
    }
}

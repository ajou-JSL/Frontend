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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.databinding.ActivityMoumFindPracticeroomBinding;
import com.example.moum.databinding.ActivityMoumManageBinding;
import com.example.moum.databinding.ActivityMoumMapPracticeroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.moum.adapter.MoumPracticeroomAdapter;
import com.example.moum.viewmodel.moum.MoumFindPracticeroomViewModel;
import com.example.moum.viewmodel.moum.MoumMapPracticeroomViewModel;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MoumMapPracticeroomActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MoumMapPracticeroomViewModel viewModel;
    private ActivityMoumMapPracticeroomBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<Practiceroom> practicerooms = new ArrayList<>();
    private Integer practiceroomId;
    private Integer memberId;
    private Integer teamId;
    private Integer moumId;
    private Integer leaderId;
    private NaverMap naverMap;
    private Practiceroom practiceroom;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumMapPracticeroomBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumMapPracticeroomViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        practiceroomId = prevIntent.getIntExtra("practiceroomId", -1);
        teamId = prevIntent.getIntExtra("teamId", -1);
        moumId = prevIntent.getIntExtra("moumId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        if(practiceroomId == -1 || teamId == -1 || moumId == -1 || leaderId == -1){
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
        viewModel.loadPracticeroom(practiceroomId);

        /*연습실 정보 불러오기 결과 감시*/
        viewModel.getIsLoadPracticeroomSuccess().observe(this, isLoadPracticeroomSuccess -> {
            Validation validation = isLoadPracticeroomSuccess.getValidation();
            Practiceroom loadedPracticeroom = isLoadPracticeroomSuccess.getData();
            if(validation == Validation.PRACTICE_ROOM_GET_SUCCESS){
                practiceroom = loadedPracticeroom;
                if(loadedPracticeroom.getName() != null) binding.textviewPracticeroomName.setText(loadedPracticeroom.getName());
                if(loadedPracticeroom.getPrice() != null) binding.textviewPracticeroomPrice.setText(String.format("%d", loadedPracticeroom.getPrice()));
                if(loadedPracticeroom.getCapacity() != null) binding.textviewPracticeroomCapacity.setText(String.format("%d", loadedPracticeroom.getCapacity()));
                if(loadedPracticeroom.getAddress() != null) binding.textviewPracticeroomAddress.setText(loadedPracticeroom.getAddress());
                if(loadedPracticeroom.getOwner() != null) binding.textviewPracticeroomOwner.setText(loadedPracticeroom.getOwner());
                if(loadedPracticeroom.getPhone() != null) binding.textviewPracticeroomPhone.setText(loadedPracticeroom.getPhone());
                if(loadedPracticeroom.getEmail() != null) binding.textviewPracticeroomEmail.setText(loadedPracticeroom.getEmail());
                if(loadedPracticeroom.getStand() != null) binding.textviewPracticeroomStand.setText(String.format("%d", loadedPracticeroom.getStand()));
                if(loadedPracticeroom.getHasPiano() != null && loadedPracticeroom.getHasPiano()) {
                    binding.checkboxPracticeroomPiano.setText("있음");
                    binding.checkboxPracticeroomPiano.setChecked(true);
                    binding.checkboxPracticeroomPiano.setEnabled(false);
                }
                if(loadedPracticeroom.getHasAmp() != null && loadedPracticeroom.getHasAmp()) {
                    binding.checkboxPracticeroomAmp.setText("있음");
                    binding.checkboxPracticeroomAmp.setChecked(true);
                    binding.checkboxPracticeroomAmp.setEnabled(false);
                }
                if(loadedPracticeroom.getHasSpeaker() != null && loadedPracticeroom.getHasSpeaker()) {
                    binding.checkboxPracticeroomSpeaker.setText("있음");
                    binding.checkboxPracticeroomSpeaker.setChecked(true);
                    binding.checkboxPracticeroomSpeaker.setEnabled(false);
                }
                if(loadedPracticeroom.getHasDrums() != null && loadedPracticeroom.getHasDrums()) {
                    binding.checkboxPracticeroomDrum.setText("있음");
                    binding.checkboxPracticeroomDrum.setChecked(true);
                    binding.checkboxPracticeroomDrum.setEnabled(false);
                }
                if(practiceroom.getImageUrls() != null && !practiceroom.getImageUrls().isEmpty()) {
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_more_rounded_gray_size_fit2)
                            .error(R.drawable.background_more_rounded_gray_size_fit2))
                            .load(loadedPracticeroom.getImageUrls().get(0)).into(binding.imageviewPracticeroom);
                    binding.imageviewPracticeroom.setClipToOutline(true);
                }
                if(loadedPracticeroom.getLatitude() != null && loadedPracticeroom.getLongitude() != null)
                    if(Boolean.TRUE.equals(viewModel.getIsNaverMapReady().getValue())){
                        Marker marker = new Marker();
                        marker.setPosition(new LatLng(loadedPracticeroom.getLatitude(), loadedPracticeroom.getLongitude()));
                        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(loadedPracticeroom.getLatitude(), loadedPracticeroom.getLongitude()));
                        naverMap.moveCamera(cameraUpdate);
                        marker.setMap(naverMap);
                        marker.setCaptionText(loadedPracticeroom.getName());
                        binding.buttonGotoNaverMap.setEnabled(true);
                    }
                if(loadedPracticeroom.getMapUrl() != null)
                    binding.buttonGotoNaverMap.setEnabled(true);
                else
                    binding.buttonGotoNaverMap.setEnabled(false);
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
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(practiceroom.getMapUrl()));
                startActivity(intent);
            }
        });

        /*모음에 등록하기 버튼 클릭*/
        binding.buttonAddInMoum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setMapType(NaverMap.MapType.Basic);
        viewModel.setIsNaverMapReady(true);

    }
}

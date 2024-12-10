package com.example.moum.view.moum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.moum.R;
import com.example.moum.data.entity.MoumPracticeroom;
import com.example.moum.data.entity.Practiceroom;
import com.example.moum.databinding.ActivityMoumListPracticeroomBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.moum.adapter.PracticeOfMoumAdapter;
import com.example.moum.viewmodel.moum.MoumListPracticeroomViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MoumListPracticeroomActivity extends AppCompatActivity {
    private MoumListPracticeroomViewModel viewModel;
    private ActivityMoumListPracticeroomBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private Integer memberId;
    private Integer teamId;
    private Integer moumId;
    private Integer leaderId;
    private ArrayList<MoumPracticeroom> practiceroomsOfMoum = new ArrayList<>();
    private ArrayList<Practiceroom> practicerooms = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumListPracticeroomBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumListPracticeroomViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*모음 id 정보 불러오기*/
        Intent prevIntent = getIntent();
        teamId = prevIntent.getIntExtra("teamId", -1);
        moumId = prevIntent.getIntExtra("moumId", -1);
        leaderId = prevIntent.getIntExtra("leaderId", -1);
        if (teamId == -1 || moumId == -1 || leaderId == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
//            finish();
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

        /*모음의 공연장 스피너 연결*/
        RecyclerView recyclerView = binding.recyclerPracticeroomOfMoum;
        PracticeOfMoumAdapter practiceOfMoumAdapter = new PracticeOfMoumAdapter();
        practiceOfMoumAdapter.setPracticerooms(practicerooms, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setAdapter(practiceOfMoumAdapter);

        /*모음의 연습실 정보 불러오기*/
        viewModel.loadPracticeroomsOfMoum(moumId);

        /*모음의 연습실 정보 불러오기 결과 감시*/
        viewModel.getIsLoadPracticeroomsOfMoumSuccess().observe(this, isLoadPracticeroomsOfMoumSuccess -> {
            Validation validation = isLoadPracticeroomsOfMoumSuccess.getValidation();
            List<MoumPracticeroom> loadedPracticeroomsOfMoum = isLoadPracticeroomsOfMoumSuccess.getData();
            if (validation == Validation.MOUM_PRACTICE_ROOM_GET_SUCCESS) {
                practiceroomsOfMoum.clear();
                practicerooms.clear();
                practiceroomsOfMoum.addAll(loadedPracticeroomsOfMoum);
                practicerooms.add(new Practiceroom()); // 연습실 찾기로 이동하는 버튼 용도의 practiceroom 하나 추가
                for (MoumPracticeroom moumPracticeroom : loadedPracticeroomsOfMoum) {
                    if (moumPracticeroom.getRoomId() != null) // 관리자에게 등록된 연습실
                    {
                        viewModel.loadPracticeroom(moumPracticeroom.getRoomId());
                    } else {
                        Practiceroom practiceroom = new Practiceroom(); // 관리자에게 등록되지 않은 연습실
                        practiceroom.setName(moumPracticeroom.getRoomName());
                        practicerooms.add(0, practiceroom);
                    }
                }
                practiceOfMoumAdapter.notifyDataSetChanged();
            } else if (validation == Validation.MOUM_PRACTICE_ROOM_NOT_FOUND) {
                practicerooms.clear();
                practicerooms.add(null);
                practiceOfMoumAdapter.notifyItemInserted(0);
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "공연장 리스트를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });

        /*연습실 상세정보 불러오기 결과 감시*/
        Disposable practiceroomDisposable = viewModel.getIsLoadPracticeroomSuccess().subscribe(isLoadPracticeroomSuccess -> {
            Validation validation = isLoadPracticeroomSuccess.getValidation();
            Practiceroom loadedPracticeroom = isLoadPracticeroomSuccess.getData();
            if (validation == Validation.PRACTICE_ROOM_GET_SUCCESS) {
                practicerooms.add(0, loadedPracticeroom);
                practiceOfMoumAdapter.notifyDataSetChanged();
            } else if (validation == Validation.MOUM_PRACTICE_ROOM_NOT_FOUND) {
                Toast.makeText(context, "모음의 연습실 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "연습실 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 감시 결과");
            }
        });
        compositeDisposable.add(practiceroomDisposable);

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadPracticeroomsOfMoum(moumId);
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void onPracticeroomClicked(Integer practiceroomId) {
        Intent intent = new Intent(MoumListPracticeroomActivity.this, MoumMapPracticeroomActivity.class);
        intent.putExtra("practiceroomId", practiceroomId);
        intent.putExtra("moumId", moumId);
        intent.putExtra("teamId", teamId);
        intent.putExtra("leaderId", leaderId);
        startActivity(intent);
    }

    public void onPracticeFindButtonClicked() {
        Intent intent = new Intent(MoumListPracticeroomActivity.this, MoumFindPracticeroomActivity.class);
        intent.putExtra("moumId", moumId);
        intent.putExtra("teamId", teamId);
        intent.putExtra("leaderId", leaderId);
        startActivity(intent);
    }
}

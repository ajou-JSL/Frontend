package com.example.moum.view.moum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.moum.adapter.MoumPracticeroomAdapter;
import com.example.moum.view.moum.adapter.MoumUpdateImageAdapter;
import com.example.moum.viewmodel.moum.MoumFindPracticeroomViewModel;
import com.example.moum.viewmodel.moum.MoumManageViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoumFindPracticeroomActivity extends AppCompatActivity {
    private MoumFindPracticeroomViewModel viewModel;
    private ActivityMoumFindPracticeroomBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<Practiceroom> practicerooms = new ArrayList<>();
    private Integer memberId;
    private Integer teamId;
    private Integer moumId;
    private Integer leaderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoumFindPracticeroomBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MoumFindPracticeroomViewModel.class);
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
            finish();
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

        /*연습실 리사이클러뷰 연결*/
        RecyclerView recyclerView = binding.recyclerPracticeroom;
        MoumPracticeroomAdapter moumPracticeroomAdapter = new MoumPracticeroomAdapter();
        moumPracticeroomAdapter.setPracticerooms(practicerooms, context);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(moumPracticeroomAdapter);

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
            if(validation == Validation.DELETE_MOUM_SUCCESS) { //TODO 바꿔야해
                practicerooms.clear();
                practicerooms.addAll(loadedPracticerooms);
                moumPracticeroomAdapter.notifyDataSetChanged();
            }
            else if(validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "검색에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "감시 결과를 알 수 없습니다.");
            }
        });
    }

    public void onPracticeroomClicked(Integer practiceroomId){
        Intent intent = new Intent(MoumFindPracticeroomActivity.this, MoumMapPracticeroomActivity.class);
        intent.putExtra("practiceroomId", practiceroomId);
        intent.putExtra("teamId", teamId);
        intent.putExtra("moumId", moumId);
        intent.putExtra("leaderId", leaderId);
        startActivity(intent);
    }
}

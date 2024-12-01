package com.example.moum.view.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityChatCreateChatroomBinding;
import com.example.moum.databinding.ActivityPerformanceCreateBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.ChatCreateChatroomActivity;
import com.example.moum.view.chat.ChatCreateChatroomOnwardActivity;
import com.example.moum.viewmodel.chat.ChatCreateChatroomViewModel;
import com.example.moum.viewmodel.community.PerformanceCreateViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerformanceCreateActivity extends AppCompatActivity {
    private PerformanceCreateViewModel viewModel;
    private ActivityPerformanceCreateBinding binding;
    private Context context;
    public String TAG = getClass().toString();
    private String[] teamNameList;
    private String[] moumNameList = {};
    private List<Team> teams = new ArrayList<>();
    private List<Moum> moums = new ArrayList<>();
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerformanceCreateBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(PerformanceCreateViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
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
                // 이전 액티비티로 돌아가기
                finish();
            }
        });

        /*단체 리스트 받아오기*/
        viewModel.loadTeamsAsLeader(id);

        /*단체 리스트 받아오기 결과 감시*/
        binding.spinnerMoum.setEnabled(false);
        binding.spinnerTeam.setEnabled(false);
        viewModel.getIsLoadTeamsAsLeaderSuccess().observe(this, result -> {
            Validation validation = result.getValidation();
            List<Team> loadedTeams = result.getData();
            if(validation == Validation.GET_TEAM_LIST_SUCCESS && loadedTeams.isEmpty()) {
                Toast.makeText(context, "리더로 속한 단체가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(validation == Validation.GET_TEAM_LIST_SUCCESS) {
                teams.clear();
                teams.addAll(loadedTeams);
                int i = 0;
                teamNameList = new String[teams.size()];
                for(Team team : teams)
                    teamNameList[i++] = team.getTeamName();

                /*group 스피너 Adapter 연결*/
                Spinner teamSpinner = binding.spinnerTeam;
                ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNameList);
                teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                teamSpinner.setAdapter(teamAdapter);
                teamSpinner.setSelection(0, false);
                viewModel.setTeamSelected(teams.get(0));

                teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        viewModel.setTeamSelected(teams.get(position));
                        binding.errorTeam.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        return;
                    }
                });
                binding.spinnerTeam.setEnabled(true);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패 from loadGroups()");
            }
            else{
                Toast.makeText(context, "단체 리스트를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation from loadGroups()");
            }
        });

        /*팀 선택 시 모음 목록 업데이트*/
        viewModel.getTeamSelected().observe(this, teamSelected -> {
            binding.spinnerMoum.setEnabled(false);
            viewModel.loadMoumsOfTeam(teamSelected.getTeamId());
        });

        /*모음 목록 조회 결과 감시*/
        viewModel.getIsLoadMoumsOfTeamSuccess().observe(this, isLoadMoumsOfTeamSuccess -> {
            Validation validation = isLoadMoumsOfTeamSuccess.getValidation();
            List<Moum> loadedMoums = isLoadMoumsOfTeamSuccess.getData();
            if(validation == Validation.GET_MOUM_SUCCESS && !loadedMoums.isEmpty()){
                moums.clear();
                moums.addAll(loadedMoums);
                int i = 0;
                moumNameList = new String[moums.size()+1];
                moumNameList[i++] = "선택안함";
                for(Moum moum : moums)
                    moumNameList[i++] = moum.getMoumName();

                /*moum 스피너 Adapter 연결*/
                Spinner moumSpinner = binding.spinnerMoum;
                ArrayAdapter<String> moumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, moumNameList);
                moumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                moumSpinner.setAdapter(moumAdapter);
                moumSpinner.setSelection(0, false);
                viewModel.setMoumSelected(moums.get(0));
                moumSpinner.setOnItemSelectedListener(null);
                moumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0) viewModel.setMoumSelected(null);
                        else viewModel.setMoumSelected(moums.get(position-1));
                        binding.errorTeam.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        return;
                    }
                });
                binding.spinnerMoum.setEnabled(true);
            }
            else if(validation == Validation.GET_MOUM_SUCCESS){
                moums.clear();
                viewModel.setMoumSelected(null);
            }
            else if(validation == Validation.TEAM_NOT_FOUND){
                Toast.makeText(context, "모음을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "단체를 불러올 수 없습니다.");
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패");
            }
            else{
                Toast.makeText(context, "모음을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        /*다음 버튼 클릭 시 이벤트*/
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Team team = viewModel.getTeamSelectedValue();
                Moum moum = viewModel.getMoumSelectedValue();
                if(team == null){
                    Toast.makeText(context, "단체를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(PerformanceCreateActivity.this, PerformanceCreateOnwardActivity.class);
                intent.putExtra("teamId", team.getTeamId());
                if(moum != null) intent.putExtra("moumId", moum.getMoumId());
                startActivity(intent);
                finish();
            }
        });
    }
}

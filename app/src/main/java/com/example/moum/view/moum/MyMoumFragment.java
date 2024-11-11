package com.example.moum.view.moum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moum.R;
import com.example.moum.data.entity.Chatroom;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.FragmentChatroomBinding;
import com.example.moum.databinding.FragmentMyMoumBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.chat.adapter.ChatroomAdapter;
import com.example.moum.view.moum.adapter.TeamAdapter;
import com.example.moum.viewmodel.moum.MyMoumViewModel;
import com.example.moum.viewmodel.chat.ChatroomViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyMoumFragment extends Fragment {

    private FragmentMyMoumBinding binding;
    private MyMoumViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ArrayList<Moum>> moums = new ArrayList<>(); //팀별 모음 리스트를 관리하는 이중 리스트

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyMoumViewModel viewModel = new ViewModelProvider(this).get(MyMoumViewModel.class);
        binding = FragmentMyMoumBinding.inflate(inflater, container, false);
        context = getContext();
        View root = binding.getRoot();

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        /*단체 viewPager 연결*/
        ViewPager2 viewpagerTeam = binding.viewpagerTeam;
        TeamAdapter teamAdapter = new TeamAdapter();
        teamAdapter.setTeamsNMoums(teams, moums, context);
        viewpagerTeam.setAdapter(teamAdapter);

        /*속한 단체 리스트 불러오기*/
        viewModel.loadTeamsAsMember(id);

        /*속한 단체 리스트 불러오기 결과 감시*/
        //TODO validation 더 추가 해야함
        viewModel.getIsLoadTeamsAsMemberSuccess().observe(getViewLifecycleOwner(), isLoadTeamsAsMemberSuccess -> {
            Validation validation = isLoadTeamsAsMemberSuccess.getValidation();
            List<Team> loadedTeams = isLoadTeamsAsMemberSuccess.getData();
            if(validation == Validation.GET_MY_TEAM_LIST_SUCCESS && !loadedTeams.isEmpty()){
                teams.addAll(loadedTeams);
                Team emptyTeam = new Team();
                teams.add(emptyTeam);
                teamAdapter.notifyItemInserted(teams.size()-1);
            }
            else if(validation == Validation.GET_MY_TEAM_LIST_SUCCESS){
                Team emptyTeam = new Team();
                teams.add(emptyTeam);
                teamAdapter.notifyItemInserted(teams.size()-1);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패");
            }
            else{
                Toast.makeText(context, "단체를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        /*viewPager 페이지 전환 시, 팀의 모음 리스트 가져오기*/
        viewpagerTeam.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int pos = viewpagerTeam.getCurrentItem();
                if(pos != teams.size()-1)
                    viewModel.loadMoumsOfTeam(teams.get(pos).getTeamId());
            }
        });

        /*팀의 모음 리스트 가져오기 결과 감시*/
        viewModel.getIsLoadMoumsOfTeamSuccess().observe(getViewLifecycleOwner(), isLoadMoumsOfTeamSuccess -> {
            Validation validation = isLoadMoumsOfTeamSuccess.getValidation();
            List<Moum> loadedMoums = isLoadMoumsOfTeamSuccess.getData();
            int pos = viewpagerTeam.getCurrentItem();
            if(validation == Validation.GET_MOUM_SUCCESS && !loadedMoums.isEmpty()){
                moums.get(pos).addAll(loadedMoums);
                Moum emptyMoum = new Moum();
                moums.get(pos).add(emptyMoum);
                teamAdapter.notifyItemChanged(pos);
            }
            else if(validation == Validation.GET_MOUM_SUCCESS){
                Moum emptyMoum = new Moum();
                moums.get(pos).add(emptyMoum);
                teamAdapter.notifyItemChanged(pos);
            }
            else if(validation == Validation.TEAM_NOT_FOUND){
               // Toast.makeText(context, "단체를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "단체를 불러올 수 없습니다.");
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패");
            }
            else{
                Toast.makeText(context, "단체를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        /*왼쪽, 오른쪽 이동 버튼 이벤트*/
        binding.imageviewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewpagerTeam.getCurrentItem();
                if(currentItem > 0)
                    viewpagerTeam.setCurrentItem(currentItem-1, true);
            }
        });

        binding.imageviewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewpagerTeam.getCurrentItem();
                if(currentItem < teamAdapter.getItemCount()-1)
                    viewpagerTeam.setCurrentItem(currentItem+1, true);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
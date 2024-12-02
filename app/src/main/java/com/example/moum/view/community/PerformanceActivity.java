package com.example.moum.view.community;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Performance;
import com.example.moum.data.entity.Team;
import com.example.moum.databinding.ActivityPerformanceBinding;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.TimeManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.adapter.ParticipantNotSelectableAdapter;
import com.example.moum.view.dialog.MoumDeleteDialog;
import com.example.moum.view.dialog.PerformDeleteDialog;
import com.example.moum.view.moum.MoumManageActivity;
import com.example.moum.view.moum.MoumUpdateActivity;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.view.profile.TeamProfileFragment;
import com.example.moum.viewmodel.community.PerformanceViewModel;

import java.util.ArrayList;

public class PerformanceActivity extends AppCompatActivity {
    private ActivityPerformanceBinding binding;
    private PerformanceViewModel viewModel;
    private Context context;
    private final String TAG = getClass().toString();
    SharedPreferenceManager sharedPreferenceManager;
    private Integer performId;
    private Performance performance;
    private Team team;
    private ArrayList<Member> members = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerformanceBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(PerformanceViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        /*이전 액티비티로부터 정보 불러오기*/
        Intent prevIntent = getIntent();
        performId = prevIntent.getIntExtra("performId", -1);
        if (performId == -1) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
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

        /*멤버 리사이클러뷰 연결*/
        RecyclerView participantRecyclerView = binding.recyclerPerformParticipants;
        ParticipantNotSelectableAdapter participantAdapter = new ParticipantNotSelectableAdapter();
        participantAdapter.setMembers(members, context);
        participantRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        participantRecyclerView.setAdapter(participantAdapter);

        /*공연 정보 불러오기*/
        viewModel.loadPerformance(performId);

        /*공연 정보 불러오기 감시 결과*/
        viewModel.getIsLoadPerformanceSuccess().observe(this, isLoadPerformanceSuccess -> {
            Validation validation = isLoadPerformanceSuccess.getValidation();
            Performance loadedPerform = isLoadPerformanceSuccess.getData();
            if (validation == Validation.PERFORMANCE_GET_SUCCESS) {
                performance = loadedPerform;
                if (loadedPerform.getPerformanceImageUrl() != null)
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_gray)
                            .error(R.drawable.background_gray))
                            .load(loadedPerform.getPerformanceImageUrl()).into(binding.imageviewPerform);
                if (loadedPerform.getPerformanceName() != null)
                    binding.textviewPerformName.setText(loadedPerform.getPerformanceName());
                if (loadedPerform.getPerformanceDescription() != null)
                    binding.textviewPerformDescription.setText(loadedPerform.getPerformanceDescription());
                if (loadedPerform.getPerformanceLocation() != null)
                    binding.textviewPerformPlace.setText(loadedPerform.getPerformanceLocation());
                if (loadedPerform.getPerformancePrice() != null)
                    binding.textviewPerformPrice.setText(String.format("%d", loadedPerform.getPerformancePrice()));
                if (loadedPerform.getPerformanceStartDate() != null && loadedPerform.getPerformanceEndDate() != null)
                    binding.textviewPerformTime.setText(String.format("%s ~ %s", TimeManager.strToDate(loadedPerform.getPerformanceStartDate()), TimeManager.strToDate(loadedPerform.getPerformanceEndDate())));
                else if (loadedPerform.getPerformanceStartDate() == null && loadedPerform.getPerformanceEndDate() != null)
                    binding.textviewPerformTime.setText(String.format("%s", TimeManager.strToDate(loadedPerform.getPerformanceEndDate())));
                else if (loadedPerform.getPerformanceStartDate() != null && loadedPerform.getPerformanceEndDate() == null)
                    binding.textviewPerformTime.setText(String.format("%s", TimeManager.strToDate(loadedPerform.getPerformanceStartDate())));
                else
                    binding.textviewPerformTime.setText("");
                if(loadedPerform.getLikesCount() != null)
                    binding.textviewLikes.setText(String.format("%d", loadedPerform.getLikesCount()));
                if(loadedPerform.getViewCount() != null)
                    binding.textviewViews.setText(String.format("%d", loadedPerform.getViewCount()));

                /*단체 정보 불러오기*/
                viewModel.loadTeam(performance.getTeamId());
            } else if (validation == Validation.ILLEGAL_ARGUMENT) {
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "공연 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*단체 정보 불러오기 감시 결과*/
        viewModel.getIsLoadTeamSuccess().observe(this, isLoadTeamSuccess -> {
            Validation validation = isLoadTeamSuccess.getValidation();
            Team loadedTeam = isLoadTeamSuccess.getData();
            if (validation == Validation.GET_TEAM_SUCCESS) {
                team = loadedTeam;
                if (loadedTeam.getFileUrl() != null)
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                            .error(R.drawable.background_more_rounded_gray_size_fit))
                            .load(loadedTeam.getFileUrl()).into(binding.imageviewPerformTeamProfile);
                binding.imageviewPerformTeamProfile.setClipToOutline(true);
                if (loadedTeam.getTeamName() != null)
                    binding.textviewPerformTeamName.setText(loadedTeam.getTeamName());
                if (loadedTeam.getDescription() != null)
                    binding.textviewPerformTeamDescription.setText(loadedTeam.getDescription());
                if (loadedTeam.getMembers() != null && !loadedTeam.getMembers().isEmpty()) {
                    for (Member member : loadedTeam.getMembers()) {
                        for (Integer participantId : performance.getMembersId()) {
                            if (member.getId().equals(participantId)) {
                                members.add(member);
                            }
                        }
                    }
                    participantAdapter.notifyItemInserted(members.size() - 1);
                }
                binding.imageviewPerformTeamProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTeamClicked(loadedTeam.getTeamId());
                    }
                });
            }
            else if (validation == Validation.TEAM_NOT_FOUND) {
                Toast.makeText(context, "팀을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "팀 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*설정 드롭다운 설정*/
        ImageView dropdownMenu = binding.buttonEtc;
        String[] etcList = getResources().getStringArray(R.array.perform_etc_list);
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!id.equals(team.getLeaderId())) return;
                PopupMenu popupMenu = new PopupMenu(PerformanceActivity.this, dropdownMenu);
                for (int i = 0; i < etcList.length; i++) {
                    popupMenu.getMenu().add(etcList[i]);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selectedItem = menuItem.getTitle().toString();
                        if (selectedItem.equals("신고하기")) {
                           //TODO 신고하기
                        } else if (selectedItem.equals("URL 복사하기")) {
                            //TODO 복사하기
                        }else if (selectedItem.equals("수정하기")) {
                            Intent intent = new Intent(PerformanceActivity.this, PerformanceUpdateActivity.class);
                            intent.putExtra("performId", performId);
                            intent.putExtra("teamId", team.getTeamId());
                            startActivity(intent);
                        }else if (selectedItem.equals("삭제하기")) {
                            PerformDeleteDialog performDeleteDialog = new PerformDeleteDialog(context, performance.getPerformanceName());
                            performDeleteDialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        /*공연 삭제 결과 감시*/
        viewModel.getIsDeletePerformanceSuccess().observe(this, isDeletePerformanceSuccess -> {
            Validation validation = isDeletePerformanceSuccess.getValidation();
            if (validation == Validation.PERFORMANCE_DELETE_SUCCESS) {
                Toast.makeText(context, "공연 게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (validation == Validation.ILLEGAL_ARGUMENT) {
                Toast.makeText(context, "유효하지 않은 데이터입니다.", Toast.LENGTH_SHORT).show();
            }
            else if (validation == Validation.NO_AUTHORITY) {
                Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "공연 게시글을 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*좋아요 버튼 설정*/
        //TODO

        /*찜 버튼 설정*/
        //TODO


    }

    public void onParticipantClicked(Integer memberId){
        MemberProfileFragment memberProfileFragment = new MemberProfileFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("targetMemberId", memberId);
        memberProfileFragment.setArguments(bundle);
        memberProfileFragment.show(getSupportFragmentManager(), memberProfileFragment.getTag());
    }

    public void onTeamClicked(Integer teamId){
        TeamProfileFragment teamProfileFragment = new TeamProfileFragment(context);
        Bundle bundle = new Bundle();
        bundle.putInt("targetTeamId", teamId);
        teamProfileFragment.setArguments(bundle);
        teamProfileFragment.show(getSupportFragmentManager(), teamProfileFragment.getTag());
    }

    public void onPerformDeleteDialogYesClicked(){
        viewModel.deletePerformance(performId);
    }
}

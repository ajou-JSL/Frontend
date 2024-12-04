package com.example.moum.view.myinfo;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.databinding.FragmentMyInformationBinding;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.moum.MoumManageActivity;
import com.example.moum.view.moum.MoumPromote1Activity;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.viewmodel.myinfo.MyInformationViewModel;

public class MyInformationFragment extends Fragment {
    private FragmentMyInformationBinding binding;
    private SharedPreferenceManager sharedPreferenceManager;
    private final String TAG = getClass().toString();
    private MyInformationViewModel viewModel;
    private Context context;
    private Member member;
    private static final int REQUEST_CODE = 300;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MyInformationViewModel.class);
        binding = FragmentMyInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext();

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        Integer id = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        String name = sharedPreferenceManager.getCache(getString(R.string.user_name_key), "no-memberName");
        if(accessToken.isEmpty() || accessToken.equals("no-access-token")){
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);

        }

        /*개인 프로필 정보 불러오기*/
        viewModel.loadMemberProfile(id);

        /*개인 프로필 정보 불러오기 결과 감시*/
        viewModel.getIsLoadMemberProfileSuccess().observe(getViewLifecycleOwner(), isLoadMemberProfileSuccess -> {
            Validation validation = isLoadMemberProfileSuccess.getValidation();
            Member tMember = isLoadMemberProfileSuccess.getData();
            if(validation == Validation.GET_PROFILE_SUCCESS){
                member = tMember;
                binding.textviewNickname.setText(tMember.getName());
                binding.textviewDescription.setText(tMember.getProfileDescription());
                if(ImageManager.isUrlValid(tMember.getProfileImageUrl()))
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .placeholder(R.drawable.background_circle_gray_size_fit)
                                    .error(R.drawable.background_circle_gray_size_fit))
                            .load(tMember.getProfileImageUrl())
                            .into(binding.imageviewProfile);
                int color = context.getColor(R.color.bronze);
                if(tMember.getTier().equals("BRONZE")){
                    color = context.getColor(R.color.bronze);
                }
                else if(tMember.getTier().equals("SILVER")){
                    color = context.getColor(R.color.silver);
                }
                else if(tMember.getTier().equals("GOLD")){
                    color = context.getColor(R.color.gold);
                }
                else if(tMember.getTier().equals("PLATINUM")){
                    color = context.getColor(R.color.platinum);
                }
                else if(tMember.getTier().equals("DIAMOND")){
                    color = context.getColor(R.color.diamond);
                }
                binding.imageviewProfile.setBorderWidth(8);
                binding.imageviewProfile.setBorderColor(color);
                binding.imageviewTier.setColorFilter(color);
            }
            else if(validation == Validation.NETWORK_FAILED){
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.e(TAG, "프로필을 불러올 수 없습니다.");
                Toast.makeText(context, "결과를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*프로필 클릭 시 이벤트*/
        final MemberProfileFragment memberProfileFragment = new MemberProfileFragment(getContext());
        binding.imageviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("targetMemberId", id);
                memberProfileFragment.setArguments(bundle);
                memberProfileFragment.show(getParentFragmentManager(), memberProfileFragment.getTag());
            }
        });

        /*수정하기 버튼*/
        binding.buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyInformationUpdateActivity.class);
                context.startActivity(intent);
            }
        });

        /*내 활동 버튼*/
        binding.buttonMyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyInfoActivityActivity.class);
                context.startActivity(intent);
            }
        });

        /*신고 및 문의 버튼*/
        binding.buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyInfoReportNQuestionActivity.class);
                context.startActivity(intent);
            }
        });

        /*로그아웃 및 회원탈퇴 버튼*/
        binding.buttonLogoutSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyInfoLogoutNSignoutActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadMemberProfile(id);
                binding.swipeRefreshLayout.setRefreshing(false);
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
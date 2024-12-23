package com.example.moum.view.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Performance;
import com.example.moum.databinding.FragmentHomeBinding;
import com.example.moum.utils.ImageManager;
import com.example.moum.utils.SharedPreferenceManager;
import com.example.moum.utils.Validation;
import com.example.moum.utils.WrapContentLinearLayoutManager;
import com.example.moum.view.auth.InitialActivity;
import com.example.moum.view.community.BoardFreeDetailActivity;
import com.example.moum.view.community.BoardRecruitDetailActivity;
import com.example.moum.view.community.PerformanceActivity;
import com.example.moum.view.home.adapter.HomeArticleHotAdapter;
import com.example.moum.view.home.adapter.HomeBannerAdapter;
import com.example.moum.view.home.adapter.HomeMoumAdapter;
import com.example.moum.view.home.adapter.HomePerformHotAdapter;
import com.example.moum.view.profile.MemberProfileFragment;
import com.example.moum.viewmodel.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private SharedPreferenceManager sharedPreferenceManager;
    private HomeViewModel viewModel;
    private Context context;
    private final ArrayList<Drawable> banners = new ArrayList<>();
    private final ArrayList<Article> articles = new ArrayList<>();
    private final ArrayList<Moum> moums = new ArrayList<>();
    private final ArrayList<Performance> performances = new ArrayList<>();
    private Integer memberId;
    private final String TAG = getClass().toString();

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getContext();

        /*자동로그인 정보를 SharedPreference에서 불러오기*/
        sharedPreferenceManager = new SharedPreferenceManager(context, getString(R.string.preference_file_key));
        String accessToken = sharedPreferenceManager.getCache(getString(R.string.user_access_token_key), "no-access-token");
        String username = sharedPreferenceManager.getCache(getString(R.string.user_username_key), "no-memberId");
        memberId = sharedPreferenceManager.getCache(getString(R.string.user_id_key), -1);
        if (accessToken.isEmpty() || accessToken.equals("no-access-token")) {
            Toast.makeText(context, "로그인 정보가 없어 초기 페이지로 돌아갑니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InitialActivity.class);
            startActivity(intent);
        }

        /*배너 리사이클러뷰 연결*/
        RecyclerView bannerRecyclerView = binding.recyclerMainBanner;
        HomeBannerAdapter bannerAdapter = new HomeBannerAdapter();
        bannerAdapter.setBanners(banners, context);
        bannerRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        bannerRecyclerView.setAdapter(bannerAdapter);
        banners.add(context.getDrawable(R.drawable.image_example_team));
        bannerAdapter.notifyItemInserted(0);

        /*실시간 인기글 리사이클러뷰 연결*/
        RecyclerView articleHotRecyclerView = binding.recyclerMainPopularPost;
        HomeArticleHotAdapter articleHotAdapter = new HomeArticleHotAdapter();
        articleHotAdapter.setArticles(articles, context, this);
        articleHotRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        articleHotRecyclerView.setAdapter(articleHotAdapter);

        /*모음 리사이클러뷰 연결*/
        RecyclerView moumRecyclerView = binding.recyclerMainMoum;
        HomeMoumAdapter moumAdapter = new HomeMoumAdapter();
        moumAdapter.setMoums(moums, context);
        moumRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        moumRecyclerView.setAdapter(moumAdapter);

        /*공연 리사이클러뷰 연결*/
        RecyclerView performanceHotRecyclerView = binding.recyclerMainPerformance;
        HomePerformHotAdapter performanceHotAdapter = new HomePerformHotAdapter();
        performanceHotAdapter.setPerformances(performances, context, this);
        performanceHotRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        performanceHotRecyclerView.setAdapter(performanceHotAdapter);

        /*정보들 불러오기*/
        viewModel.loadArticlesHot();
        viewModel.loadMoums(memberId);
        viewModel.loadPerformsHot();
        viewModel.loadMemberProfile(memberId);

        /*실시간 인기글 가져오기 감시 결과*/
        viewModel.getIsLoadArticlesHotSuccess().observe(getViewLifecycleOwner(), isLoadArticlesHotSuccess -> {
            Validation validation = isLoadArticlesHotSuccess.getValidation();
            List<Article> loadedArticles = isLoadArticlesHotSuccess.getData();
            if (validation == Validation.ARTICLE_LIST_GET_SUCCESS && loadedArticles.isEmpty()) {
                articles.clear();
            } else if (validation == Validation.ARTICLE_LIST_GET_SUCCESS) {
                articles.clear();
                articles.addAll(loadedArticles);
                articleHotAdapter.notifyItemInserted(articles.size() - 1);
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패");
            } else {
                Toast.makeText(context, "실시간 인기글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        /*모음 정보 가져오기 감시 결과*/
        viewModel.getIsLoadMoumsSuccess().observe(getViewLifecycleOwner(), isLoadMoumsSuccess -> {
            Validation validation = isLoadMoumsSuccess.getValidation();
            List<Moum> loadedMoums = isLoadMoumsSuccess.getData();
            List<Moum> ingMoums = new ArrayList<>();
            if (loadedMoums != null) {
                for (Moum loadedMoum : loadedMoums) {
                    if (!loadedMoum.getProcess().getFinishStatus()) {
                        ingMoums.add(loadedMoum);
                    }
                }
            }
            if (validation == Validation.GET_MOUM_SUCCESS && ingMoums.isEmpty()) {
                moums.clear();
            } else if (validation == Validation.GET_MOUM_SUCCESS) {
                moums.clear();
                moums.addAll(loadedMoums);
                moumAdapter.notifyItemInserted(moums.size() - 1);
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패");
            } else {
                Toast.makeText(context, "나의 모음을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        /*공연 게시글 가져오기 감시 결과*/
        viewModel.getIsLoadPerformsHotSuccess().observe(getViewLifecycleOwner(), isLoadPerformsHotSuccess -> {
            Validation validation = isLoadPerformsHotSuccess.getValidation();
            List<Performance> loadedPerforms = isLoadPerformsHotSuccess.getData();
            if (validation == Validation.PERFORMANCE_HOT_LIST_GET_SUCCESS && loadedPerforms.isEmpty()) {
                performances.clear();
            } else if (validation == Validation.PERFORMANCE_HOT_LIST_GET_SUCCESS) {
                performances.clear();
                performances.addAll(loadedPerforms);
                performanceHotAdapter.notifyItemInserted(performances.size() - 1);
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "호출 실패");
            } else {
                Toast.makeText(context, "공연 게시글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "알 수 없는 validation");
            }
        });

        /*개인 프로필 정보 불러오기 결과 감시*/
        viewModel.getIsLoadMemberProfileSuccess().observe(getViewLifecycleOwner(), isLoadMemberProfileSuccess -> {
            Validation validation = isLoadMemberProfileSuccess.getValidation();
            Member tMember = isLoadMemberProfileSuccess.getData();
            if (validation == Validation.GET_PROFILE_SUCCESS) {
                if (ImageManager.isUrlValid(tMember.getProfileImageUrl())) {
                    Glide.with(context)
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .placeholder(R.drawable.background_circle_darkgray_size_fit)
                                    .error(R.drawable.background_circle_darkgray_size_fit))
                            .load(tMember.getProfileImageUrl())
                            .into(binding.imageProfile);
                }
            } else if (validation == Validation.NETWORK_FAILED) {
                Toast.makeText(context, "호출에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "프로필을 불러올 수 없습니다.");
                Toast.makeText(context, "프로필을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /*내 프로필 띄우기*/
        final MemberProfileFragment memberProfileFragment = new MemberProfileFragment(getContext());
        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("targetMemberId", memberId);
                memberProfileFragment.setArguments(bundle);
                memberProfileFragment.show(getParentFragmentManager(), memberProfileFragment.getTag());
            }
        });

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadArticlesHot();
                viewModel.loadMoums(memberId);
                viewModel.loadPerformsHot();
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

    public void onArticleClicked(Integer articleId, String category) {
        Intent intent = new Intent(context, BoardFreeDetailActivity.class);
        if (category.equals("RECRUIT_BOARD")) {
            intent = new Intent(context, BoardRecruitDetailActivity.class);
        }
        intent.putExtra("targetBoardId", articleId);
        context.startActivity(intent);
    }

    public void onPerformClicked(Integer performId) {
        Intent intent = new Intent(context, PerformanceActivity.class);
        intent.putExtra("performId", performId);
        context.startActivity(intent);
    }
}
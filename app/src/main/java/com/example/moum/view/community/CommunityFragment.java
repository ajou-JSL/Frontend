package com.example.moum.view.community;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moum.R;
import com.example.moum.databinding.FragmentCommunityBinding;
import com.example.moum.utils.RefreshableFragment;
import com.example.moum.view.community.adapter.TabbarPagerAdapter;
import com.example.moum.viewmodel.community.CommunityViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;
    private CommunityViewModel communityViewModel;
    private TabbarPagerAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);

        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // TabbarPagerAdapter 초기화
        adapter = new TabbarPagerAdapter(getChildFragmentManager(), getLifecycle());

        initSearchButton();
        initTabLayout(binding.communityTabbarPage, binding.tabLayout, adapter);

        // swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int currentItem = binding.communityTabbarPage.getCurrentItem();
                Fragment fragment = getChildFragmentManager().findFragmentByTag("f" + currentItem);
                if (fragment instanceof RefreshableFragment) {
                    ((RefreshableFragment) fragment).refreshContent();
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }



    private void initSearchButton() {
        binding.searchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommunitySearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTabLayout(ViewPager2 pager, TabLayout tabLayout, TabbarPagerAdapter adapter) {
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("자유게시판");
                        break;
                    case 1:
                        tab.setText("모집게시판");
                        break;
                    case 2:
                        tab.setText("단체 탐색");
                        break;
                    case 3:
                        tab.setText("멤버 탐색");
                        break;
                    case 4:
                        tab.setText("공연 탐색");
                        break;
                }
            }
        }).attach();

        // 선택 시 텍스트 굵게 설정
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabTextView = (TextView) tab.view.getChildAt(1);
                tab.view.setBackgroundResource(R.drawable.background_tablayout_click);
                communityViewModel.setSelectedTabIndex(tab.getPosition());

                if (tabTextView != null) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 선택 해제된 탭의 텍스트를 기본으로 설정
                tab.view.setBackgroundResource(R.drawable.background_bottom_line);
                TextView tabTextView = (TextView) tab.view.getChildAt(1);
                if (tabTextView != null) {
                    tabTextView.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 재선택 시 추가 처리 필요 없다면 비워둠
                TextView tabTextView = (TextView) tab.view.getChildAt(1);
                tab.view.setBackgroundResource(R.drawable.background_tablayout_click);
                communityViewModel.setSelectedTabIndex(tab.getPosition());

                if (tabTextView != null) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                }
            }
        });

        pager.setCurrentItem(communityViewModel.getSelectedTabIndex().getValue(), false);
        tabLayout.selectTab(tabLayout.getTabAt(communityViewModel.getSelectedTabIndex().getValue()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onCommunityIndexReturn(Integer communityIndex){
        Log.e("ss", "onCommunityIndexReturn start");
        if(communityIndex != -1) {
            communityViewModel.setSelectedTabIndex(communityIndex);
            binding.communityTabbarPage.setCurrentItem(communityViewModel.getSelectedTabIndex().getValue(), false);
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(communityViewModel.getSelectedTabIndex().getValue()));
            Log.e("ss", "setSelectedTabIndex success");
        }
    }
}
package com.example.moum.view.bottomnavi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moum.databinding.FragmentCommunityBinding;
import com.example.moum.view.community.adapter.TabbarPagerAdapter;
import com.example.moum.viewmodel.bottomnavi.CommunityViewModel;
import com.example.moum.view.community.CommunitySearchActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommunityViewModel communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);

        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initSearchButton();


        //tablayout setting
        TabbarPagerAdapter adapter = new TabbarPagerAdapter(getChildFragmentManager(), getLifecycle());
        binding.communityTabbarPage.setAdapter(adapter);
        ViewPager2 pager = binding.communityTabbarPage;
        TabLayout tabLayout = binding.tabLayout;

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
                        tab.setText("공연 탐색");
                        break;
                }
            }
        }).attach();
        pager.setCurrentItem(2);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
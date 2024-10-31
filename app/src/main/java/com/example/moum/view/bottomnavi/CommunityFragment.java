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

        ViewPager2 pager = binding.communityTabbarPage;
        TabLayout tabLayout = binding.tabLayout;


        pager.setOffscreenPageLimit(2);
        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> {tab.setText("Tab " + (position + 1));}).attach();
        pager.setAdapter(new PageAdapter(this));



        return root;
    }



    private void initSearchButton() {
        binding.searchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommunitySearchActivity.class); // 새로운 액티비티로 이동
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
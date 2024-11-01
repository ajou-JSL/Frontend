package com.example.moum.view.community;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabbarPagerAdapter extends FragmentStateAdapter {

    public TabbarPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BoardFreeFragment();
            case 1:
                return new BoardRecruitFragment();
            case 2:
                return new BoardGroupFragment();
            case 3:
                return new BoardPerformanceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

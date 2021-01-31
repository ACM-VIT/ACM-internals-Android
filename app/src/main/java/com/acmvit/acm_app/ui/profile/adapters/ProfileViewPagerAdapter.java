package com.acmvit.acm_app.ui.profile.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.acmvit.acm_app.ui.profile.ProjectsFragment;
import com.acmvit.acm_app.ui.profile.SocialsFragment;

public class ProfileViewPagerAdapter extends FragmentStateAdapter {

    public ProfileViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new ProjectsFragment();
        } else {
            fragment = new SocialsFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

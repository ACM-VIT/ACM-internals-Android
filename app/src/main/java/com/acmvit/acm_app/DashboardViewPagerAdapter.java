package com.acmvit.acm_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class DashboardViewPagerAdapter extends FragmentStateAdapter {

    private static final int N_PAGES = 3;
    ArrayList<Idea> ideas, currentProjects, allProjects;

    public DashboardViewPagerAdapter(
        @NonNull Fragment fragment,
        ArrayList<Idea> ideas,
        ArrayList<Idea> currentProjects,
        ArrayList<Idea> allProjects
    ) {
        super(fragment);
        this.ideas = ideas;
        this.currentProjects = currentProjects;
        this.allProjects = allProjects;
        //Log.i("UpcomingAdapter", "The contestsAll size in UpcomingAdapter2 is " + this.contestsAll.size());
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        //Log.i("UpcomingAdapter", "The contestsAll size in UpcomingAdapter is " + contestsAll.size());
        switch (position) {
            case 0:
                return DashboardIdeasFragment.newInstance(ideas);
            case 1:
                return DashboardCurrentProjectsFragment.newInstance(
                    currentProjects
                );
            case 2:
                return DashboardAllProjectsFragment.newInstance(allProjects);
        }
        return DashboardIdeasFragment.newInstance(ideas);
    }

    @Override
    public int getItemCount() {
        return N_PAGES;
    }
}

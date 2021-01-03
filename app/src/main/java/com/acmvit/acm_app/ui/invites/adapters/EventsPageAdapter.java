package com.acmvit.acm_app.ui.invites.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.acmvit.acm_app.ui.invites.NotificationFragment;
import com.acmvit.acm_app.ui.invites.RequestsFragment;

public class EventsPageAdapter extends FragmentStateAdapter {

    private static final int SIZE = 2;

    public EventsPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new NotificationFragment();
        }
        return new RequestsFragment();
    }

    @Override
    public int getItemCount() {
        return SIZE;
    }
}

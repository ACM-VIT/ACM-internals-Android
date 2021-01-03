package com.acmvit.acm_app.ui.invites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.FragmentEventsBinding;
import com.acmvit.acm_app.ui.invites.adapters.EventsPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private static final String[] TAB_TEXT = { "Notifications", "Requests" };

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        EventsPageAdapter adapter = new EventsPageAdapter(this);
        binding.eventViewpager.setAdapter(adapter);
        new TabLayoutMediator(
            binding.eventsTabLayout,
            binding.eventViewpager,
            (tab, position) -> {
                tab.setText(TAB_TEXT[position]);
            }
        )
            .attach();
    }
}

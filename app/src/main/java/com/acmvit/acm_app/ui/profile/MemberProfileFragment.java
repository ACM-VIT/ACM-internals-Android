package com.acmvit.acm_app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.acmvit.acm_app.databinding.FragmentProfileBinding;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.acmvit.acm_app.ui.base.BaseViewModelFactory;
import com.acmvit.acm_app.ui.profile.adapters.ProfileViewPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class MemberProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.fab.setVisibility(View.INVISIBLE);
        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        BaseViewModelFactory factory = new BaseViewModelFactory(
                (BaseActivity) getActivity()
        );
        ProfileViewModel viewModel = (ProfileViewModel) new ViewModelProvider(this, factory).get("member profile",ProfileViewModel.class);
        binding.setViewModel(viewModel);
        MemberProfileFragmentArgs args = MemberProfileFragmentArgs.fromBundle(
                requireArguments()
        );
        viewModel.fetchData(args.getUid());
        new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Projects");
                    }
                    if (position == 1) {
                        tab.setText("Socials");
                    }
                }
        )
                .attach();
        super.onViewCreated(view, savedInstanceState);
    }
}
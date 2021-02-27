package com.acmvit.acm_app.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.acmvit.acm_app.ui.base.BaseViewModelFactory;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.FragmentProfileBinding;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.acmvit.acm_app.ui.base.BaseViewModelFactory;
import com.acmvit.acm_app.ui.profile.ProfileViewPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private FragmentProfileBinding binding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
        @NotNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_profile_to_editProfileFragment));
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
        viewModel =
            new ViewModelProvider(this, factory).get(ProfileViewModel.class);
        binding.setViewModel(viewModel);
        ProfileFragmentArgs args = ProfileFragmentArgs.fromBundle(
            requireArguments()
        );
        if (args.getUid().equals("default")) {
            viewModel.initializeData();
            binding.fab.setVisibility(View.VISIBLE);
        } else {
            viewModel.fetchData(args.getUid());
            binding.fab.setVisibility(View.INVISIBLE);
        }

        super.onViewCreated(view, savedInstanceState);

        setupOverflowMenu();
    }

    public void setupOverflowMenu() {
        Context wrapper = new ContextThemeWrapper(
                getContext(),
                R.style.ThemeOverlay_popupTheme
        );
        PopupMenu popup = new PopupMenu(wrapper, binding.overflowMenu);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(
                menuItem -> {
                    if (menuItem.getItemId() == R.id.menu_signout) {
                        viewModel.logout();
                        return true;
                    }
                    return false;
                }
        );

        binding.overflowMenu.setOnClickListener(
                view -> popup.show()
        );
    }
}

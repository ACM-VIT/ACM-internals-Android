package com.acmvit.acm_app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.acmvit.acm_app.BaseViewModelFactory;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.FragmentEditProfileBinding;
import com.acmvit.acm_app.service.AuthService;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.google.android.material.snackbar.Snackbar;
import net.openid.appauth.AuthorizationServiceConfiguration;
import org.jetbrains.annotations.NotNull;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private FragmentEditProfileBinding binding;
    private static final int RC_REQUEST = 101;
    private EditProfileViewModel viewModel;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
        @NotNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        Log.d(TAG, "onCreateView: ");

        binding =
            FragmentEditProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.inputName.getEditText().clearFocus();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Init Viewmodel
        BaseViewModelFactory factory = new BaseViewModelFactory(
            (BaseActivity) getActivity()
        );
        viewModel =
            new ViewModelProvider(this, factory)
            .get(EditProfileViewModel.class);

        binding.setViewmodel(viewModel);
        initObservers();
    }

    private void initObservers() {
        viewModel
            .getStartResultActivity()
            .observe(
                getViewLifecycleOwner(),
                intent -> {
                    startActivityForResult(intent, RC_REQUEST);
                }
            );
    }

    @Override
    public void onActivityResult(
        int requestCode,
        int resultCode,
        @Nullable Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_REQUEST) {
            viewModel.processIntent(data);
            Log.d(TAG, "onActivityResult: " + data);
        }
    }
}

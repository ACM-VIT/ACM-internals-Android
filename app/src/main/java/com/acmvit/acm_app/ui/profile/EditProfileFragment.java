package com.acmvit.acm_app.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acmvit.acm_app.BaseViewModelFactory;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.FragmentEditProfileBinding;
import com.acmvit.acm_app.service.AuthService;
import com.acmvit.acm_app.util.Constants;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private static final int RC_LOGIN = 101;
    private AuthorizationServiceConfiguration authServiceConfig;
    private AuthService authService;
    private EditProfileViewModel viewModel;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        authService = AuthService.getInstance();

        //Init Viewmodel
        BaseViewModelFactory factory = new BaseViewModelFactory(getActivity());
        viewModel = new ViewModelProvider(this, factory).get(EditProfileViewModel.class);

        initObservers();
    }

    private void initObservers() {
        viewModel.getStartResultActivity().observe(getViewLifecycleOwner(), intent -> {
            startActivityForResult(intent, RC_LOGIN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_LOGIN){
            viewModel.sendTokenFromIntent(data);
        }
    }
}
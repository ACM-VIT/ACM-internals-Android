package com.acmvit.acm_app.ui.profile;

import android.accounts.Account;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.Accounts;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.repository.UserRepository;
import com.acmvit.acm_app.service.AuthService;
import com.acmvit.acm_app.ui.auth.LoginViewModel;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.Status;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class EditProfileViewModel extends BaseViewModel {
    public enum State {
        STANDBY,
        DISCORD_LOG_IN,
        SEND_TOKEN,
        ERROR
    }

    private final MutableLiveData<State> state = new MutableLiveData<>(State.STANDBY);
    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final MutableLiveData<Accounts> accounts = new MutableLiveData<>();
    private final MutableLiveData<Intent> startResultActivity = new MutableLiveData<>();

    public EditProfileViewModel(ActivityViewModel activityViewModel, Application application) {
        super(activityViewModel, application);
        userRepository = UserRepository.getInstance();
        sessionManager = AcmApp.getSessionManager();
        accounts.setValue(sessionManager.getUserDetails().getAccounts());
    }

    public void signInWithDiscord() {
        if (activityViewModel.canRunNetworkTask() &&
                (state.getValue() == State.STANDBY || state.getValue() == State.ERROR)) {
            state.setValue(State.DISCORD_LOG_IN);
            activityViewModel.setIsLoading(true);
            startResultActivity.setValue(AuthService.getInstance()
                    .getDiscordFlowIntent(application));
        }
    }

    public void sendTokenFromIntent(Intent data){
        if (state.getValue() == State.DISCORD_LOG_IN) {
            sendDiscordTokenFromIntent(data);
        }
    }

    public void sendDiscordTokenFromIntent(Intent data) {
        if (activityViewModel.canRunNetworkTask()) {
            state.setValue(State.SEND_TOKEN);
            activityViewModel.setIsLoading(true);
                LiveData<Resource<UserData>> authData =
                        userRepository.addDiscordUsingIntent(application, data);
                authData.observeForever(authDataResource -> {
                    UserData userData = authDataResource.data;
                    if (authDataResource.status == Status.SUCCESS && data != null) {
                        accounts.setValue(userData.getUser().getAccounts());
                        state.setValue(State.STANDBY);
                    } else {
                        state.setValue(State.ERROR);
                    }
                });
        }
    }

    public MutableLiveData<Intent> getStartResultActivity() {
        return startResultActivity;
    }

    public void setError() {
        this.state.setValue(State.ERROR);
        activityViewModel.setIsLoading(false);
    }

    public LiveData<State> getState() {
        return state;
    }

    public LiveData<Accounts> getAccounts() {
        return accounts;
    }
}

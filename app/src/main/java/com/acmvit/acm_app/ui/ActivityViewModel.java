package com.acmvit.acm_app.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.util.Action;
import com.acmvit.acm_app.util.reactive.SingleLiveEvent;

public class ActivityViewModel extends ViewModel {

    private final SessionManager sessionManager = AcmApp.getSessionManager();
    private final LiveData<Boolean> loginState = sessionManager.getAuthStateNotifier();
    private final LiveData<Boolean> networkState = AcmApp.getIsConnected();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(
            false
    );
    private final SingleLiveEvent<Action> action = new SingleLiveEvent<>();

    public boolean canRunNetworkTask() {
        return networkState.getValue();
    }

    public boolean canRunAuthenticatedNetworkTask() {
        return canRunNetworkTask() && sessionManager.getAuthState();
    }

    public LiveData<Boolean> getLoginState() {
        return loginState;
    }

    public LiveData<Boolean> getNetworkState() {
        return networkState;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.setValue(isLoading);
    }

    public LiveData<Action> getAction() {
        return action;
    }

    public void fireAction(Action action) {
        this.action.setValue(action);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public boolean checkLocking() {
        if (getSessionManager().getUserDetails() != null) {
            return getSessionManager().getUserDetails().getAccounts().getDiscord() == null;
        }
        return true;
    }
}

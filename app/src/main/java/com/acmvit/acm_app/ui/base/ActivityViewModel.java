package com.acmvit.acm_app.ui.base;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.ui.auth.LoginViewModel;

public class ActivityViewModel extends ViewModel {

    private final SessionManager sessionManager = AcmApp.getSessionManager();
    private final LiveData<Boolean> loginState = sessionManager.getAuthStateNotifier();
    private final LiveData<Boolean> networkState = AcmApp.getIsConnected();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public boolean canRunNetworkTask(){
        return networkState.getValue() && loginState.getValue();
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
}

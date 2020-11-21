package com.acmvit.acm_app.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;

public class LoginViewModel extends BaseViewModel {
    public enum State{
        STANDBY,
        LOG_IN,
        GET_ACCESS_CODE,
        ERROR
    }
    private final SessionManager sessionManager = AcmApp.getSessionManager();
    private final ActivityViewModel activityViewModel;
    private final MutableLiveData<State> state = new MutableLiveData<>(State.STANDBY);

    public LoginViewModel(ActivityViewModel activityViewModel) {
        super(activityViewModel);
        this.activityViewModel = activityViewModel;
    }

    public void signInWithGoogle(){
        if(activityViewModel.canRunNetworkTask()
                && state.getValue()!=State.LOG_IN && !sessionManager.getAuthState()){
            state.setValue(State.LOG_IN);
            activityViewModel.setIsLoading(true);
        }
    }

    public void getAccessCode(String authCode){

    }

    public void setError() {
        this.state.setValue(State.ERROR);
        activityViewModel.setIsLoading(false);
    }

    public LiveData<State> getState() {
        return state;
    }
}

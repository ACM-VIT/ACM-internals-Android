package com.acmvit.acm_app.ui.auth;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.repository.AuthRepository;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.Status;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginViewModel extends BaseViewModel {
    private static final String TAG = "LoginViewModel";
    public enum State{
        STANDBY,
        LOG_IN,
        GET_ACCESS_CODE,
        ERROR
    }

    private final SessionManager sessionManager = AcmApp.getSessionManager();
    private final AuthRepository authRepository;
    private final MutableLiveData<State> state = new MutableLiveData<>(State.STANDBY);

    public LoginViewModel(ActivityViewModel activityViewModel, Application application) {
        super(activityViewModel, application);
        authRepository = AuthRepository.getInstance();
    }

    public void signInWithGoogle(){
        if(activityViewModel.canRunNetworkTask()
                && state.getValue()!=State.LOG_IN && !sessionManager.getAuthState()){
            state.setValue(State.LOG_IN);
            activityViewModel.setIsLoading(true);
        }
    }

    public void getAccessCode(String idToken){
        if(idToken != null && !sessionManager.getAuthState()){
            state.setValue(State.GET_ACCESS_CODE);
            activityViewModel.setIsLoading(true);
            LiveData<Resource<UserData>> authData = authRepository.loginByGoogle(idToken);
            authData.observeForever(authDataResource -> {
                UserData data = authDataResource.data;
                if(authDataResource.status == Status.SUCCESS && data != null){
                    sessionManager.addUserDetails(data.getUser());
                    sessionManager.addToken(data.getToken());
                    state.setValue(State.STANDBY);
                }else{
                    state.setValue(State.ERROR);
                }
            });
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account == null){
                setError();
                return;
            }
            getAccessCode(account.getIdToken());

        } catch (ApiException e) {
            setError();
            Log.e(TAG, "handleSignInResult: " + e.getStatusCode(), e);
        }
    }

    public void setError() {
        this.state.setValue(State.ERROR);
        activityViewModel.setIsLoading(false);
    }

    public LiveData<State> getState() {
        return state;
    }
}

package com.acmvit.acm_app.ui;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.acmvit.acm_app.repository.AuthRepository;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.reactive.SingleTimeObserver;

public class MainViewModel extends BaseViewModel {

    private static final String TAG = "MainViewModel";

    private enum State {
        STANDBY,
        LOGOUT,
    }

    private final AuthRepository authRepository;
    private final ActivityViewModel activityViewModel;
    private State state = State.STANDBY;

    public MainViewModel(
        ActivityViewModel activityViewModel,
        Application application
    ) {
        super(activityViewModel, application);
        authRepository = AuthRepository.getInstance();
        this.activityViewModel = activityViewModel;
    }

    public void logout() {
        if (
            activityViewModel.canRunAuthenticatedNetworkTask() &&
            state == State.STANDBY
        ) {
            Log.d(TAG, "logout: ");
            state = State.LOGOUT;
            activityViewModel.setIsLoading(true);
            LiveData<Resource<Void>> status = authRepository.logout();
            new SingleTimeObserver<Resource<Void>>() {
                @Override
                public void onReceived(Resource<Void> resource) {
                    activityViewModel.setIsLoading(false);
                    state = State.STANDBY;
                }
            }
            .attachTo(status);
        }
    }
}

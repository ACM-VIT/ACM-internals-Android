package com.acmvit.acm_app.ui.splash;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.repository.UserRepository;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.ui.profile.EditProfileViewModel;
import com.acmvit.acm_app.util.Action;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.SingleTimeObserver;
import com.acmvit.acm_app.util.Status;

public class SplashViewModel extends AndroidViewModel {

    private static final String TAG = "SplashViewModel";
    private static final int SPLASH_TIMEOUT = 2500;
    private final SessionManager sessionManager;
    private final UserRepository userRepository;
    private final Application application;
    private final MutableLiveData<Boolean> canNavigate = new MutableLiveData<>(
        false
    );

    public SplashViewModel(Application application) {
        super(application);
        this.sessionManager = AcmApp.getSessionManager();
        userRepository = UserRepository.getInstance();
        this.application = application;
    }

    public void startNavigationIntent() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> canNavigate.setValue(true), SPLASH_TIMEOUT);
    }

    public void fetchUserDetails() {
        if (sessionManager.getAuthState()) {
            userRepository.fetchUserUsingWM(application);
        }
    }

    public LiveData<Boolean> getCanNavigate() {
        return canNavigate;
    }
}

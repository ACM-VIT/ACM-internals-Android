package com.acmvit.acm_app.ui.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.acmvit.acm_app.AcmApp;

public class BaseViewModel extends ViewModel {

    protected final ActivityViewModel activityViewModel;
    protected final Application application;

    public BaseViewModel(
        ActivityViewModel activityViewModel,
        Application application
    ) {
        this.activityViewModel = activityViewModel;
        this.application = application;
    }

    public ActivityViewModel getActivityViewModel() {
        return activityViewModel;
    }
}

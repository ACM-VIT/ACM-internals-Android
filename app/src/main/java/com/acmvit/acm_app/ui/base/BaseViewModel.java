package com.acmvit.acm_app.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acmvit.acm_app.AcmApp;

public class BaseViewModel extends ViewModel {
    protected final ActivityViewModel activityViewModel;

    public BaseViewModel(ActivityViewModel activityViewModel) {
        this.activityViewModel = activityViewModel;
    }

    public ActivityViewModel getActivityViewModel() {
        return activityViewModel;
    }
}

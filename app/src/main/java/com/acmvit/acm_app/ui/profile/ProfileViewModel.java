package com.acmvit.acm_app.ui.profile;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;

public class ProfileViewModel extends BaseViewModel {

    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> disp = new MutableLiveData<>("");
    public final MutableLiveData<String> dp = new MutableLiveData<>("");
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public ProfileViewModel(ActivityViewModel activityViewModel, Application application) {
        super(activityViewModel, application);
        initializeData();
    }

    private void initializeData() {
        User user = activityViewModel.getSessionManager().getUserDetails();
        if (user != null) {
            this.user.setValue(user);
            name.setValue(user.getName());
            disp.setValue(user.getDisp());
            dp.setValue(user.getDp());
        }
    }


}

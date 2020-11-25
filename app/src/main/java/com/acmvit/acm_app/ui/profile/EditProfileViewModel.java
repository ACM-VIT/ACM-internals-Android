package com.acmvit.acm_app.ui.profile;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.AcmApp;
import com.acmvit.acm_app.model.Accounts;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.pref.SessionManager;
import com.acmvit.acm_app.repository.UserRepository;
import com.acmvit.acm_app.service.AuthService;
import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.Action;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.SingleLiveEvent;
import com.acmvit.acm_app.util.SingleTimeObserver;
import com.acmvit.acm_app.util.Status;

public class EditProfileViewModel extends BaseViewModel {
    private static final String TAG = "EditProfileViewModel";

    public enum State {
        STANDBY,
        DISCORD_LOG_IN,
        SEND_TOKEN,
        ERROR,
        PIC_CHOOSE,
        UPDATE_USER
    }

    private final MutableLiveData<State> state = new MutableLiveData<>(State.STANDBY);
    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> disp = new MutableLiveData<>("");
    public final MutableLiveData<String> dp = new MutableLiveData<>("");
    private final MutableLiveData<Accounts> accounts = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final SingleLiveEvent<Intent> startResultActivity = new SingleLiveEvent<>();

    public EditProfileViewModel(ActivityViewModel activityViewModel, Application application) {
        super(activityViewModel, application);
        userRepository = UserRepository.getInstance();
        sessionManager = AcmApp.getSessionManager();
        initializeData();
    }

    private void initializeData() {
        User user = sessionManager.getUserDetails();
        if (user != null) {
            accounts.setValue(user.getAccounts());
            this.user.setValue(user);
            name.setValue(user.getName());
            disp.setValue(user.getDisp());
            dp.setValue(user.getDp());
        }
    }

    public void startPicChooserActivity() {
        if (!canRun()) {
            return;
        }
        setState(State.PIC_CHOOSE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startResultActivity.setValue(intent);
    }

    public void signInWithDiscord() {
        if (activityViewModel.canRunAuthenticatedNetworkTask() && canRun()) {

            setState(State.DISCORD_LOG_IN);
            Intent i = AuthService.getInstance()
                    .getDiscordFlowIntent(application);
            if (i != null) {
                startResultActivity.setValue(i);
            } else {
                setState(State.ERROR);
            }
        }
    }

    public void processIntent(Intent data) {
        if (data == null) {
            setState(State.STANDBY);
            return;
        }
        if (state.getValue() == State.DISCORD_LOG_IN) {
            sendDiscordTokenFromIntent(data);
        } else if (state.getValue() == State.PIC_CHOOSE) {
            setDpFromIntent(data);
        }
    }

    private void setDpFromIntent(Intent data) {
        dp.setValue(data.getData().toString());
        setState(State.STANDBY);
    }

    private void sendDiscordTokenFromIntent(Intent data) {
        if (activityViewModel.canRunAuthenticatedNetworkTask()) {
            setState(State.SEND_TOKEN);
            LiveData<Resource<UserData>> authData =
                    userRepository.addDiscordUsingIntent(application, data);
            new UserDataObserver().attachTo(authData);
        }else {
            setState(State.STANDBY);
        }
    }

    public void updateUser() {
        activityViewModel.setAction(new Action(Action.MainEvent.HIDE_KEYBOARD));
        if(canRun() && activityViewModel.canRunAuthenticatedNetworkTask()) {
            setState(State.UPDATE_USER);
            User user = sessionManager.getUserDetails();
            String namev = name.getValue();
            String dispv = disp.getValue();
            String dpv = dp.getValue();

            if (namev == null) {
                return;
            }
            if (dispv == null) {
                dispv = "";
            }

            LiveData<Resource<UserData>> updateUser;
            if (!TextUtils.isEmpty(dpv) && !dpv.equals(user.getDp())) {
                updateUser = userRepository.updateUser(namev, dispv, dpv);
            } else if (!namev.equals(user.getName()) &&
                    !dispv.equals(user.getDisp())) {
                updateUser = userRepository.updateUser(namev, dispv);
            } else {
                setState(State.STANDBY);
                return;
            }
            new UserDataObserver().attachTo(updateUser);
        }
    }

    public LiveData<Intent> getStartResultActivity() {
        return startResultActivity;
    }

    public LiveData<State> getState() {
        return state;
    }

    public LiveData<Accounts> getAccounts() {
        return accounts;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setState(State state) {
        this.state.setValue(state);
        activityViewModel.setIsLoading(!(state == State.ERROR || state == State.STANDBY));
    }

    public boolean canRun() {
        return (state.getValue() == State.STANDBY || state.getValue() == State.ERROR);
    }

    private class UserDataObserver extends SingleTimeObserver<Resource<UserData>> {
        @Override
        public void onReceived(Resource<UserData> userResource) {
            UserData userData = userResource.data;
            if (userResource.status == Status.SUCCESS && userData != null) {
                accounts.setValue(userData.getUser().getAccounts());
                activityViewModel.setAction(new Action(Action.MainEvent.SNACKBAR,
                        "Updated Successfully"));
                setState(State.STANDBY);
            } else {
                setState(State.ERROR);
                activityViewModel.setAction(new Action(Action.MainEvent.SNACKBAR,
                        "Unable to update"));
            }
        }
    }

}

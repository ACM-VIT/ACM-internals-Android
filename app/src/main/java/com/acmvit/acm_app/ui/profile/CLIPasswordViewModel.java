package com.acmvit.acm_app.ui.profile;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.repository.UserRepository;
import com.acmvit.acm_app.ui.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.Action;
import com.acmvit.acm_app.util.Constants;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.Status;
import com.acmvit.acm_app.util.reactive.ReactiveUtils;
import com.acmvit.acm_app.util.reactive.SingleLiveEvent;
import com.acmvit.acm_app.util.reactive.SingleTimeObserver;

public class CLIPasswordViewModel extends BaseViewModel {

    private static final String TAG = "CLIPasswordViewModel";

    public enum State {
        STANDBY,
        SET_PWD,
    }

    private final UserRepository userRepository;
    private final MutableLiveData<State> state = new MutableLiveData<>(
        State.STANDBY
    );

    public final MutableLiveData<String> password = new MutableLiveData<>("");
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>(
        ""
    );
    public final LiveData<Boolean> isMismatchError = ReactiveUtils.joinLD(
        password,
        confirmPassword,
        value ->
            !password.getValue().equals(confirmPassword.getValue()) &&
            !confirmPassword.getValue().isEmpty()
    );

    public final LiveData<Boolean> isWeakPwdError = Transformations.map(
        password,
        input ->
            input.length() < Constants.MIN_PASSWORD_LENGTH && input.length() > 0
    );

    public final LiveData<Boolean> isPwdValid = ReactiveUtils.joinLD(
        isMismatchError,
        isWeakPwdError,
        value ->
            !isMismatchError.getValue() &&
            !isWeakPwdError.getValue() &&
            !confirmPassword.getValue().isEmpty()
    );

    private final SingleLiveEvent<Void> dismissDialog = new SingleLiveEvent<>();

    public CLIPasswordViewModel(
        ActivityViewModel activityViewModel,
        Application application
    ) {
        super(activityViewModel, application);
        userRepository = UserRepository.getInstance();

        isMismatchError.observeForever(
            aBoolean -> Log.d(TAG, "mismatchError " + aBoolean)
        );
        isWeakPwdError.observeForever(
            aBoolean -> Log.d(TAG, "weakPwdError: " + aBoolean)
        );
        isPwdValid.observeForever(
            aBoolean -> Log.d(TAG, "PwdMatch: " + aBoolean)
        );

        initState();
    }

    private void initState() {
        password.setValue("");
        confirmPassword.setValue("");
        state.setValue(State.STANDBY);
    }

    public void dismissDialog() {
        dismissDialog.setValue(null);
        initState();
    }

    public SingleLiveEvent<Void> getDismissDialogLiveData() {
        return dismissDialog;
    }

    public void setPassword() {
        if (canRun() && activityViewModel.canRunAuthenticatedNetworkTask()) {
            activityViewModel.setIsLoading(true);
            new SingleTimeObserver<Resource<UserData>>() {
                @Override
                public void onReceived(Resource<UserData> userResource) {
                    activityViewModel.setIsLoading(false);
                    if (userResource.status == Status.SUCCESS) {
                        activityViewModel.fireAction(
                            new Action(
                                Action.MainEvent.SNACKBAR,
                                "CLI Password set Successfully"
                            )
                        );
                    } else {
                        activityViewModel.fireAction(
                            new Action(
                                Action.MainEvent.SNACKBAR,
                                "Error setting the password"
                            )
                        );
                    }
                }
            }
                .attachTo(userRepository.setCliPassword(password.getValue()));

            dismissDialog();
        }
    }

    private boolean canRun() {
        return State.STANDBY.equals(state.getValue());
    }
}

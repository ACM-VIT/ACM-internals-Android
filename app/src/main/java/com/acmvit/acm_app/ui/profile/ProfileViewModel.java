package com.acmvit.acm_app.ui.profile;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.model.UserData;
import com.acmvit.acm_app.network.BackendResponse;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.repository.AuthRepository;
import com.acmvit.acm_app.ui.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.reactive.SingleTimeObserver;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends BaseViewModel {

    private enum State {
        STANDBY,
        LOGOUT,
    }

    private State state = State.STANDBY;
    private final AuthRepository authRepository;
    public final MutableLiveData<String> name = new MutableLiveData<>("");
    public final MutableLiveData<String> disp = new MutableLiveData<>("");
    public final MutableLiveData<String> dp = new MutableLiveData<>("");
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public ProfileViewModel(
        ActivityViewModel activityViewModel,
        Application application
    ) {
        super(activityViewModel, application);
        authRepository = AuthRepository.getInstance();
    }

    public void initializeData() {
        User user = activityViewModel.getSessionManager().getUserDetails();
        if (user != null) {
            this.user.setValue(user);
            name.setValue(user.getName());
            disp.setValue(user.getDisp());
            dp.setValue(user.getDp());
        }
    }

    public void fetchData(String uid) {
        BackendService service = ServiceGenerator
            .getInstance()
            .createTokenizedService(BackendService.class);
        service
            .fetchUserById(uid)
            .enqueue(
                new Callback<BackendResponse<UserData>>() {
                    @Override
                    public void onResponse(
                        @NotNull Call<BackendResponse<UserData>> call,
                        @NotNull Response<BackendResponse<UserData>> response
                    ) {
                        assert response.body() != null;
                        user.setValue(response.body().getData().getUser());
                        name.setValue(
                            Objects.requireNonNull(user.getValue()).getName()
                        );
                        dp.setValue(user.getValue().getDp());
                        disp.setValue(user.getValue().getDisp());
                    }

                    @Override
                    public void onFailure(
                        @NotNull Call<BackendResponse<UserData>> call,
                        @NotNull Throwable t
                    ) {}
                }
            );
    }

    public void logout() {
        if (
            activityViewModel.canRunAuthenticatedNetworkTask() &&
            state == State.STANDBY
        ) {
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

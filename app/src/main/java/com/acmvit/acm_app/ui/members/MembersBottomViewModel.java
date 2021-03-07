package com.acmvit.acm_app.ui.members;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.db.pref.SessionManager;
import com.acmvit.acm_app.repository.MembersRepository;
import com.acmvit.acm_app.ui.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.reactive.SingleSourceMediatorLD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MembersBottomViewModel extends BaseViewModel {
    private static final String TAG = "MembersBottomViewModel";
    private final SingleSourceMediatorLD<List<User>> users = new SingleSourceMediatorLD<>();
    private final MediatorLiveData<List<User>> filteredUsers = new MediatorLiveData<>();
    private final MembersRepository membersRepository;
    private final SessionManager sessionManager;
    private final MutableLiveData<String> searchText = new MutableLiveData<>("");

    public MembersBottomViewModel(ActivityViewModel activityViewModel, Application application) {
        super(activityViewModel, application);
        sessionManager = getActivityViewModel().getSessionManager();
        membersRepository = MembersRepository.getInstance();
        users.addSource(membersRepository.getAllUsers(), usersRes -> {
            if (usersRes.data != null) {
                users.setValue(usersRes.data);
            }
        });

        Runnable filterTask = () -> {
            List<User> input = users.getValue();
            if (input == null) return;
            List<User> filteredUsersL = new ArrayList<>();
            if (searchText.getValue().isEmpty()) {
                for (User user : input) {
                    if (!user.getUser_id().equals(sessionManager.getUserDetails().getUser_id())) {
                        filteredUsersL.add(user);
                    } else {
                        filteredUsersL.add(0, user);
                    }
                }
                filteredUsers.setValue(filteredUsersL);
                return;
            }

            for (User user : input) {
                if (user.getName().toLowerCase().contains(searchText.getValue().toLowerCase())) {
                    filteredUsersL.add(user);
                }
            }

            Collections.sort(filteredUsersL, (user1, user2) -> {
                int index1 = user1.getName().toLowerCase().indexOf(searchText.getValue().toLowerCase());
                int index2 = user2.getName().toLowerCase().indexOf(searchText.getValue().toLowerCase());

                return Integer.compare(index1, index2);
            });

            filteredUsers.setValue(filteredUsersL);
        };

        filteredUsers.addSource(searchText, s -> filterTask.run());
        filteredUsers.addSource(users, i -> filterTask.run());
    }

    public LiveData<List<User>> getFilteredUsers() {
        return filteredUsers;
    }

    public MutableLiveData<String> getSearchText() {
        return searchText;
    }
}

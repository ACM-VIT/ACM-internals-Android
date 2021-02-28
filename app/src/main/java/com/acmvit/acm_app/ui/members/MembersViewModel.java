package com.acmvit.acm_app.ui.members;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModel;
import com.acmvit.acm_app.model.Accounts;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.repository.MembersRepository;
import com.acmvit.acm_app.repository.MembersRepository;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import java.util.ArrayList;
import java.util.Objects;

public class MembersViewModel extends ViewModel {

    public MutableLiveData<ArrayList<User>> list = new MutableLiveData<>();
    public MutableLiveData<ArrayList<User>> allUsers = new MutableLiveData<>();
    private final MembersRepository repository;

    public MembersViewModel() {
        repository = MembersRepository.getInstance();
        list.setValue(new ArrayList<>());
        initializeData();
    }

    public void initializeData() {
        allUsers = MembersRepository.users;
    }

    public void filterUsers(String query) {
        ArrayList<User> filtered = new ArrayList<>();
        for (
            int i = 0;
            i < Objects.requireNonNull(allUsers.getValue()).size();
            i++
        ) {
            if (
                allUsers
                    .getValue()
                    .get(i)
                    .getName()
                    .toLowerCase()
                    .startsWith(query)
            ) {
                filtered.add(allUsers.getValue().get(i));
            }
        }
        list.setValue(filtered);
    }
}

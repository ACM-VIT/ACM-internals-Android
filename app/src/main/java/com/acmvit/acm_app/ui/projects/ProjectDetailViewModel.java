package com.acmvit.acm_app.ui.projects;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.acmvit.acm_app.converters.ProjectBasicProject;
import com.acmvit.acm_app.model.BasicProject;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.ui.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;

import java.util.List;
import java.util.Objects;

public class ProjectDetailViewModel extends BaseViewModel {
    private final MutableLiveData<List<String>> tags = new MutableLiveData<>();
    private final MutableLiveData<List<User>> members = new MutableLiveData<>();
    private final MutableLiveData<String> dp = new MutableLiveData<>();
    private final MutableLiveData<BasicProject> basicProject = new MutableLiveData<>();
    private Project project;

    public ProjectDetailViewModel(ActivityViewModel activityViewModel, Application application) {
        super(activityViewModel, application);
    }

    public MutableLiveData<List<String>> getTags() {
        return tags;
    }

    public MutableLiveData<List<User>> getMembers() {
        return members;
    }

    public MutableLiveData<String> getDp() {
        return dp;
    }

    public MutableLiveData<BasicProject> getBasicProject() {
        return basicProject;
    }

    public Project generateModifiedProject() {
        return new ProjectBasicProject().entityToModel(Objects.requireNonNull(basicProject.getValue()));
    }
}

package com.acmvit.acm_app.repository;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

import com.acmvit.acm_app.db.AcmDb;
import com.acmvit.acm_app.db.Converters;
import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.ProjectStatus;
import com.acmvit.acm_app.model.ProjectsList;
import com.acmvit.acm_app.model.Tag;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.repository.converters.ProjectConverter;
import com.acmvit.acm_app.repository.converters.TagConverter;
import com.acmvit.acm_app.util.PaginatedResource;
import com.acmvit.acm_app.util.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class ProjectsRepository {
    private static final String TAG = "ProjectsRepository";
    ProjectConverter projectConverter = new ProjectConverter();
    TagConverter tagConverter = new TagConverter();
    public static ProjectsRepository instance;
    private static ServiceGenerator serviceGenerator;
    private static BackendService tokenizedService;
    private static AcmDb localDb;

    public static ProjectsRepository getInstance(AcmDb db) {
        if (instance == null) {
            instance = new ProjectsRepository();
            serviceGenerator = ServiceGenerator.getInstance();
            tokenizedService = serviceGenerator.createTokenizedService(BackendService.class);
            localDb = db;
        }
        return instance;
    }

    private ProjectsRepository() {
    }

    public PaginatedResource<Project> getAllProjects() {
        return getProjectsWithUidAndStatus(null, null);
    }

    public PaginatedResource<Project> getProjectsWithName(@NonNull String name) {
        return new PaginatedNetworkBoundResource<HashMap<String, List<Project>>, Project>(15, localDb) {
            @Override
            protected Completable saveCallResult(HashMap<String, List<Project>> item) {
                Log.d(TAG, "saveCallResult: " + item.keySet());
                return saveProjects(item.get("project"));
            }

            @Override
            protected DataSource.Factory<Integer, Project> loadFromDb() {
                return localDb.projectDao().getAllProjectsWithName(name);
            }

            @Override
            protected void createCall(MutableLiveData<Resource<HashMap<String, List<Project>>>> reqItem, int lastRequestedPage) {
                tokenizedService.getProjectsWithName(name, lastRequestedPage).enqueue(new BackendNetworkCall<>(reqItem));
            }
        }.get();
    }

    public PaginatedResource<Project> getProjectsWithTag(@NonNull String tag) {
        return new PaginatedNetworkBoundResource<ProjectsList, Project>(15, localDb) {
            @Override
            protected Completable saveCallResult(ProjectsList item) {
                Log.d(TAG, "saveCallResult: " + item.getAllProjects());
                return saveProjects(item.getAllProjects());
            }

            @Override
            protected DataSource.Factory<Integer, Project> loadFromDb() {
                return localDb.projectDao().getAllProjectsWithTag(tag);
            }

            @Override
            protected void createCall(MutableLiveData<Resource<ProjectsList>> reqItem, int lastRequestedPage) {
                tokenizedService.getProjectsWithTag(tag, lastRequestedPage).enqueue(new BackendNetworkCall<>(reqItem));
            }
        }.get();
    }

    public PaginatedResource<Project> getProjectsWithUidAndStatus(@Nullable String uid, @Nullable ProjectStatus status) {
        String statusS = Converters.projectStatusToString(status);

        return new PaginatedNetworkBoundResource<ProjectsList, Project>(15, localDb) {
            @Override
            protected Completable saveCallResult(ProjectsList item) {
                return saveProjects(item.getAllProjects());
            }

            @Override
            protected DataSource.Factory<Integer, Project> loadFromDb() {
                return localDb.projectDao().getAllProjectsWithUserStatus(status, uid);
            }

            @Override
            protected void createCall(MutableLiveData<Resource<ProjectsList>> reqItem, int lastRequestedPage) {
                if (uid == null && status == null) {
                    tokenizedService.getAllProjects(lastRequestedPage).enqueue(new BackendNetworkCall<>(reqItem));
                } else if (uid == null) {
                    tokenizedService.getProjectsWithStatus(statusS, lastRequestedPage).enqueue(new BackendNetworkCall<>(reqItem));
                } else if (status == null) {
                    tokenizedService.getProjectsWithUser(uid, lastRequestedPage).enqueue(new BackendNetworkCall<>(reqItem));
                } else {
                    tokenizedService.getProjectsWithUserAndStatus(uid, statusS, lastRequestedPage).enqueue(new BackendNetworkCall<>(reqItem));
                }
            }

            @Override
            protected Completable deleteItems() {
                if (uid != null || status != null) {
                    return Completable.complete();
                }
                return localDb.projectDao().deleteProjects();
            }
        }.get();
    }

    public LiveData<Resource<List<String>>> getAllTags() {
        return new NetworkBoundResource<Map<String, List<Tag>>, List<String>>() {
            @Override
            protected Completable saveCallResult(Map<String, List<Tag>> tagsh) {
                List<Tag> tags = tagsh.get("allTags");
                if (tags == null) {return Completable.complete();}
                return localDb.tagDao().insertTags(tags);
            }

            @Override
            protected LiveData<List<String>> loadFromDb() {
                return Transformations.map(localDb.tagDao().getAllTags(), tagConverter::entityToModel);
            }

            @Override
            protected void createCall(MutableLiveData<Resource<Map<String, List<Tag>>>> reqItem) {
                BackendNetworkCall<Map<String, List<Tag>>> call = new BackendNetworkCall<>(reqItem);
                tokenizedService.getAllTags().enqueue(call);
            }
        }.getAsLiveData();

    }

    private Completable saveProjects(List<Project> projects) {
        Log.d(TAG, "saveProjects: " + projects);
        if (projects == null) return Completable.complete();
        List<ProjectDb> projectDbs = projectConverter.modelToEntity(projects);

        Completable projectCompletable = localDb.projectDao().insertProject(projectDbs);
        Completable artifactsCompletable = Observable.fromIterable(projects)
                .switchMapCompletable(project ->
                        localDb.userDao().insertUsersAndIgnoreIfExists(project.getMembersWithFounder())
                                .andThen(localDb.tagDao().insertTags(tagConverter.modelToEntity(project.getTags())))
                                .andThen(localDb.projectMemberCrossRefDao().insert(project))
                                .andThen(localDb.projectTagCrossRefDao().insert(project)));

        return artifactsCompletable.andThen(projectCompletable);
    }

}

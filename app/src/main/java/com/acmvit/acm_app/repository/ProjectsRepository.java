package com.acmvit.acm_app.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

import com.acmvit.acm_app.converters.ProjectProjectUb;
import com.acmvit.acm_app.db.AcmDb;
import com.acmvit.acm_app.db.Converters;
import com.acmvit.acm_app.db.model.ProjectDb;
import com.acmvit.acm_app.model.ListChange;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.ProjectStatus;
import com.acmvit.acm_app.model.ProjectUpdateBody;
import com.acmvit.acm_app.model.ProjectsList;
import com.acmvit.acm_app.model.Tag;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.network.BackendNetworkCall;
import com.acmvit.acm_app.network.BackendService;
import com.acmvit.acm_app.network.ServiceGenerator;
import com.acmvit.acm_app.converters.ProjectProjectDb;
import com.acmvit.acm_app.converters.TagString;
import com.acmvit.acm_app.util.Constants;
import com.acmvit.acm_app.util.GeneralUtils;
import com.acmvit.acm_app.util.PaginatedResource;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProjectsRepository {
    private static final String TAG = "ProjectsRepository";
    private final ProjectProjectDb projectConverter = new ProjectProjectDb();
    private final TagString tagConverter = new TagString();
    private final ProjectProjectUb projectUbConverter = new ProjectProjectUb();
    public static GeneralRepository generalRepository;
    public static ProjectsRepository instance;
    private static ServiceGenerator serviceGenerator;
    private static BackendService tokenizedService;
    private static AcmDb localDb;

    public static ProjectsRepository getInstance(AcmDb db) {
        if (instance == null) {
            instance = new ProjectsRepository();
            generalRepository = GeneralRepository.getInstance();
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
                return addProjectsToDb(item.get("project"));
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
                return addProjectsToDb(item.getAllProjects());
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
                return addProjectsToDb(item.getAllProjects());
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
                if (tags == null) {
                    return Completable.complete();
                }
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

    public LiveData<Status> saveProject(Project project) {
        return saveProject(project, new Project());
    }

    public LiveData<Status> saveProject(Project project, Project oldProject) {
        ProjectUpdateBody bodyNew = projectUbConverter.modelToEntity(project);
        ProjectUpdateBody bodyOld = projectUbConverter.modelToEntity(oldProject);
        ListChange<User> userListChange = GeneralUtils.getListDiff(project.getMembers(), oldProject.getMembers());
        ListChange<String> tagListChange = GeneralUtils.getListDiff(project.getTags(), oldProject.getTags());

        Completable updateProject = Completable.complete();
        Completable addRemoveUsers;

        if (!Objects.equals(bodyNew, bodyOld)) {
            Observable<String> saveImgObservable = Observable.just("");

            if (!Objects.equals(project.getIcon(), oldProject.getIcon()) && project.getIcon() != null) {
                saveImgObservable = generalRepository.saveImageToCloud(
                        project.getIcon(), Constants.Backend.PROJECT_PIC_STORAGE_LOC, project.getProject_id())
                        .toObservable();
            }

            updateProject = saveImgObservable
                    .switchMap(s -> tokenizedService.updateProject(project.getProject_id(), bodyNew))
                    .switchMapCompletable(projectBackendResponse -> {
                        Project projectData= projectBackendResponse.getData();
                        if (projectData == null) {
                            return Completable.error(new NullPointerException());
                        }
                        return localDb.projectDao()
                                .updateProject(projectConverter.modelToEntity(projectData))
                                .mergeWith(localDb.projectTagCrossRefDao()
                                        .updateProjectTags(project.getProject_id(), tagListChange));
                    });

        }

        //TODO: Add AddRemoveUsers endpoint
        addRemoveUsers = Completable.complete()
                .andThen(localDb.projectMemberCrossRefDao().updateProjectMembers(project.getProject_id(), userListChange));

        return LiveDataReactiveStreams.fromPublisher(updateProject.mergeWith(addRemoveUsers)
                .toFlowable()
                .onErrorReturnItem(Status.ERROR)
                .last(Status.SUCCESS)
                .cast(Status.class)
                .toFlowable());
    }

    private Completable addProjectsToDb(List<Project> projects) {
        if (projects == null) return Completable.complete();
        List<ProjectDb> projectDbs = projectConverter.modelToEntity(projects);

        Completable projectCompletable = localDb.projectDao().insertProject(projectDbs);
        Completable artifactsCompletable = Observable.fromIterable(projects)
                .switchMapCompletable(project ->
                        localDb.userDao().insertUsersAndIgnoreIfExists(project.getMembersWithFounder())
                                .andThen(localDb.projectMemberCrossRefDao().insert(project))
                                .mergeWith(localDb.tagDao().insertTags(tagConverter.modelToEntity(project.getTags()))
                                .andThen(localDb.projectTagCrossRefDao().insert(project))));

        return artifactsCompletable.andThen(projectCompletable);
    }

}

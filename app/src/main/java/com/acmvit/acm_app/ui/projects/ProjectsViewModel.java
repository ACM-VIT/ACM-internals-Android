package com.acmvit.acm_app.ui.projects;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.acmvit.acm_app.db.AcmDb;
import com.acmvit.acm_app.model.Filter;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.ProjectStatus;
import com.acmvit.acm_app.model.TagSearchFilter;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.repository.ProjectsRepository;
import com.acmvit.acm_app.ui.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;
import com.acmvit.acm_app.util.GeneralUtils;
import com.acmvit.acm_app.util.PaginatedResource;
import com.acmvit.acm_app.util.Status;
import com.acmvit.acm_app.util.reactive.SingleLiveEvent;
import com.acmvit.acm_app.util.reactive.SingleSourceMediatorLD;
import com.google.android.gms.common.util.Strings;
import java.util.List;
import java.util.Objects;

public class ProjectsViewModel extends BaseViewModel {

    private static final String TAG = "'ProjectsViewModel'";

    public enum State {
        STANDBY,
    }

    private final Filters filters = new Filters();
    private final MutableLiveData<State> state = new MutableLiveData<>();
    private final SingleSourceMediatorLD<List<String>> tags = new SingleSourceMediatorLD<>();
    private final PaginatedResource<Project> projectPaginatedResource = new PaginatedResource<>();
    private final MediatorLiveData<Boolean> isRefreshing = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> isSearchExpanded = new MutableLiveData<>(
        false
    );
    private final MutableLiveData<String> searchData = new MutableLiveData<>(
        ""
    );
    private final ProjectsRepository projectsRepository;
    private final SingleLiveEvent<Void> scrollToTop = new SingleLiveEvent<>();

    public ProjectsViewModel(
        ActivityViewModel activityViewModel,
        Application application
    ) {
        super(activityViewModel, application);
        isRefreshing.setValue(false);
        projectsRepository =
            ProjectsRepository.getInstance(AcmDb.getInstance(application));
        tags.addSource(
            projectsRepository.getAllTags(),
            listResource -> {
                if (listResource.data != null) {
                    Log.d(TAG, "ProjectsViewModel: ");
                    tags.setValue(listResource.data);
                }
            }
        );

        filters
            .getTagSearchFilter()
            .observeForever(
                s -> {
                    String searchKeyVal = filters.getSearchText();

                    if (!searchKeyVal.equals(searchData.getValue())) {
                        searchData.setValue(searchKeyVal);
                    }
                }
            );

        isSearchExpanded.observeForever(
            val -> {
                if (
                    !val &&
                    !filters.getSearchText().equals(searchData.getValue())
                ) {
                    searchData.setValue(filters.getSearchText());
                }
            }
        );

        Transformations
            .distinctUntilChanged(filters.getFilter())
            .observeForever(
                filter -> {
                    scrollToTop.setValue(null);
                    projectPaginatedResource.data.setValue(null);
                    TagSearchFilter tagSearchFilter = filter.tagSearchFilter;
                    if (
                        tagSearchFilter == null ||
                        Strings.emptyToNull(tagSearchFilter.searchKey) == null
                    ) {
                        projectPaginatedResource.updatePaginatedResource(
                            projectsRepository.getProjectsWithUidAndStatus(
                                filter.userId,
                                filter.status
                            )
                        );
                    } else if (filter.tagSearchFilter.isTag) {
                        projectPaginatedResource.updatePaginatedResource(
                            projectsRepository.getProjectsWithTag(
                                tagSearchFilter.searchKey
                            )
                        );
                    } else {
                        projectPaginatedResource.updatePaginatedResource(
                            projectsRepository.getProjectsWithName(
                                tagSearchFilter.searchKey
                            )
                        );
                    }
                }
            );

        searchData.observeForever(
            s -> {
                boolean isTag = filters.getIsTag();
                if (s.isEmpty() && isTag) {
                    filters.clear(filters.tagSearchFilter);
                }
            }
        );

        isRefreshing.addSource(
            Objects.requireNonNull(projectPaginatedResource.status),
            status -> {
                if (
                    GeneralUtils.unBoxNullableBoolean(
                        isRefreshing.getValue()
                    ) &&
                    status != Status.LOADING
                ) {
                    isRefreshing.setValue(false);
                }
            }
        );

        isRefreshing.observeForever(
            isRefreshing -> {
                if (isRefreshing) {
                    projectPaginatedResource.invalidate.run();
                }
            }
        );
    }

    public Filters getFilters() {
        return filters;
    }

    public MutableLiveData<String> getSearchData() {
        return searchData;
    }

    public void setSearchViewExpand(boolean isExpanded) {
        isSearchExpanded.setValue(isExpanded);
    }

    public void removeUserFilter() {
        filters.user.setValue(null);
    }

    public LiveData<Void> getScrollToTop() {
        return scrollToTop;
    }

    public MutableLiveData<Boolean> getIsSearchExpanded() {
        return isSearchExpanded;
    }

    public void submitTextFilter() {
        if (!filters.getIsTag()) {
            String searchStr = String.valueOf(searchData.getValue());
            TagSearchFilter filter = searchStr.isEmpty()
                ? null
                : new TagSearchFilter(false, searchStr);
            filters.setTagSearchFilter(filter);
        }
        isSearchExpanded.setValue(false);
    }

    public void submitSpan(String tag) {
        searchData.setValue(tag);
        filters.setTagSearchFilter(new TagSearchFilter(true, tag));
        isSearchExpanded.setValue(false);
    }

    public MutableLiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }

    public PaginatedResource<Project> getProjectsPaginatedResource() {
        return projectPaginatedResource;
    }

    public LiveData<List<String>> getTags() {
        return tags;
    }

    public static class Filters {

        private final MutableLiveData<TagSearchFilter> tagSearchFilter = new MutableLiveData<>();
        private final MutableLiveData<ProjectStatus> projectStatus = new MutableLiveData<>();
        private final MutableLiveData<User> user = new MutableLiveData<>();
        private final MediatorLiveData<Filter> filter = new MediatorLiveData<>();

        public Filters() {
            filter.setValue(new Filter(null, null, null));
            filter.addSource(
                projectStatus,
                status -> {
                    if (status != null) {
                        clear(tagSearchFilter);
                    }
                    filter.setValue(
                        Objects
                            .requireNonNull(filter.getValue())
                            .setStatus(status)
                    );
                }
            );

            filter.addSource(
                user,
                userVal -> {
                    if (userVal != null) {
                        clear(tagSearchFilter);
                    }
                    String id = userVal == null ? null : userVal.getUser_id();
                    filter.setValue(
                        Objects.requireNonNull(filter.getValue()).setUserId(id)
                    );
                }
            );

            filter.addSource(
                tagSearchFilter,
                tagSearch -> {
                    if (tagSearch != null) {
                        clear(projectStatus, user);
                    }
                    filter.setValue(
                        Objects
                            .requireNonNull(filter.getValue())
                            .setTagSearchFilter(tagSearch)
                    );
                }
            );
        }

        private void clear(MutableLiveData<?>... liveDatas) {
            for (MutableLiveData<?> liveData : liveDatas) {
                if (liveData.getValue() == null) {
                    continue;
                }
                liveData.setValue(null);
            }
        }

        public MutableLiveData<TagSearchFilter> getTagSearchFilter() {
            return tagSearchFilter;
        }

        public MutableLiveData<ProjectStatus> getProjectStatus() {
            return projectStatus;
        }

        public MutableLiveData<User> getUser() {
            return user;
        }

        public void setProjectStatus(ProjectStatus projectStatus) {
            this.projectStatus.setValue(projectStatus);
        }

        public LiveData<Filter> getFilter() {
            return filter;
        }

        public void setTagSearchFilter(TagSearchFilter tagSearchFilter) {
            this.tagSearchFilter.setValue(tagSearchFilter);
        }

        public void setUser(User user) {
            this.user.setValue(user);
        }

        public boolean getIsTag() {
            if (tagSearchFilter.getValue() == null) return false;
            return tagSearchFilter.getValue().isTag;
        }

        public String getSearchText() {
            if (tagSearchFilter.getValue() == null) return "";
            return tagSearchFilter.getValue().searchKey;
        }
    }
}

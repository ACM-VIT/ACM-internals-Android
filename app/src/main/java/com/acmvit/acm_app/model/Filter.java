package com.acmvit.acm_app.model;

import java.util.Objects;

public class Filter {
    public final String userId;
    public final ProjectStatus status;
    public final TagSearchFilter tagSearchFilter;

    public Filter(String userId, ProjectStatus status, TagSearchFilter tagSearchFilter) {
        this.userId = userId;
        this.status = status;
        this.tagSearchFilter = tagSearchFilter;
    }

    public Filter setStatus(ProjectStatus status) {
        return new Filter(userId, status, tagSearchFilter);
    }

    public Filter setTagSearchFilter(TagSearchFilter tagSearchFilter) {
        return new Filter(userId, status, tagSearchFilter);
    }

    public Filter setUserId(String userId) {
        return new Filter(userId, status, tagSearchFilter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(userId, filter.userId) &&
                status == filter.status &&
                Objects.equals(tagSearchFilter, filter.tagSearchFilter);
    }

}

package com.acmvit.acm_app.model;

import java.util.Objects;

public class TagSearchFilter {

    public final boolean isTag;
    public final String searchKey;

    public TagSearchFilter(boolean isTag, String searchKey) {
        this.isTag = isTag;
        this.searchKey = searchKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagSearchFilter that = (TagSearchFilter) o;
        return isTag == that.isTag && Objects.equals(searchKey, that.searchKey);
    }
}

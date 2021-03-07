package com.acmvit.acm_app.model;

import java.util.List;

public class ListChange<T> {
    public final List<T> added;
    public final List<T> removed;

    public ListChange(List<T> added, List<T> removed) {
        this.added = added;
        this.removed = removed;
    }
}

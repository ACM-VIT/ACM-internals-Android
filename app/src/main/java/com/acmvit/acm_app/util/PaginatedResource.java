package com.acmvit.acm_app.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.paging.PagedList;
import com.acmvit.acm_app.util.reactive.SingleSourceMediatorLD;
import java.util.ArrayList;

public class PaginatedResource<T> {

    public final SingleSourceMediatorLD<PagedList<T>> data;
    public final SingleSourceMediatorLD<Status> status;
    public Runnable invalidate;
    public Runnable requestAgain;

    public PaginatedResource(
        @NonNull LiveData<PagedList<T>> data,
        @NonNull LiveData<Status> status,
        @NonNull Runnable invalidate,
        @NonNull Runnable requestAgain
    ) {
        this();
        this.data.addSource(data, this.data::setValue);
        this.status.addSource(status, this.status::setValue);
        this.invalidate = invalidate;
        this.requestAgain = requestAgain;
    }

    public PaginatedResource() {
        this.data = new SingleSourceMediatorLD<>();
        this.status = new SingleSourceMediatorLD<>();
        this.requestAgain = () -> {};
        this.invalidate = () -> {};
    }

    public void updatePaginatedResource(
        PaginatedResource<T> paginatedResource
    ) {
        this.data.addSource(paginatedResource.data, this.data::setValue);
        this.status.addSource(paginatedResource.status, this.status::setValue);
        this.invalidate = paginatedResource.invalidate;
        this.requestAgain = paginatedResource.requestAgain;
    }
}

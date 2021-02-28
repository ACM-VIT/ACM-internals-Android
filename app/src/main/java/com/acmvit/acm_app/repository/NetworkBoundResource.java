package com.acmvit.acm_app.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import com.acmvit.acm_app.util.PaginatedResource;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.reactive.SingleTimeObserver;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Objects;

public abstract class NetworkBoundResource<RequestType, ResultType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    public NetworkBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(
            dbSource,
            data -> {
                result.removeSource(dbSource);
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(
                        dbSource,
                        newData -> result.setValue(Resource.success(newData))
                    );
                }
            }
        );
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {
        MutableLiveData<Resource<RequestType>> apiResponse = new MutableLiveData<>();
        createCall(apiResponse);
        result.addSource(
            dbSource,
            newData -> result.setValue(Resource.loading(newData))
        );
        result.addSource(
            apiResponse,
            response -> {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);
                switch (Objects.requireNonNull(apiResponse.getValue()).status) {
                    case SUCCESS:
                        saveCallResult(processResponse(response))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                () ->
                                    result.addSource(
                                        loadFromDb(),
                                        newData ->
                                            result.setValue(
                                                Resource.success(newData)
                                            )
                                    )
                            );
                        break;
                    case ERROR:
                        onFetchFailed();
                        result.addSource(
                            dbSource,
                            newData ->
                                result.setValue(
                                    Resource.error(
                                        response.getMessage(),
                                        newData
                                    )
                                )
                        );
                        break;
                }
            }
        );
    }

    public LiveData<Resource<ResultType>> getAsLiveData() {
        return Transformations.distinctUntilChanged(result);
    }

    protected abstract Completable saveCallResult(RequestType item);

    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract void createCall(
        MutableLiveData<Resource<RequestType>> reqItem
    );

    protected boolean shouldFetch(ResultType data) {
        return true;
    }

    protected RequestType processResponse(Resource<RequestType> response) {
        return response.data;
    }

    protected void onFetchFailed() {}
}

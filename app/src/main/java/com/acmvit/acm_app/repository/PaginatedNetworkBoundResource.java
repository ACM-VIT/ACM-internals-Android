package com.acmvit.acm_app.repository;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.acmvit.acm_app.db.AcmDb;
import com.acmvit.acm_app.util.PaginatedResource;
import com.acmvit.acm_app.util.Resource;
import com.acmvit.acm_app.util.Status;
import com.acmvit.acm_app.util.reactive.SingleTimeObserver;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.util.concurrent.TimeUnit;

public abstract class PaginatedNetworkBoundResource<RequestType, ResultModel> {

    private static final String TAG = "PaginatedNetworkBoundRe";
    private final PaginatedResource<ResultModel> paginatedResource;
    private AcmDb localDb;
    private final MutableLiveData<Boolean> shouldPushUpdates = new MutableLiveData<>(
        true
    );

    public PaginatedNetworkBoundResource(int databasePageSize) {
        DataSource.Factory<Integer, ResultModel> dataSourceFactory = loadFromDb();
        BoundaryCallback boundaryCallback = new BoundaryCallback();

        PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(databasePageSize + 1)
            .build();

        LiveData<Status> status = LiveDataReactiveStreams.fromPublisher(
            boundaryCallback
                .getStatus()
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST)
        );

        final LiveData<PagedList<ResultModel>> dbSource = new LivePagedListBuilder<>(
            dataSourceFactory,
            config
        )
            .setBoundaryCallback(boundaryCallback)
            .build();

        LiveData<PagedList<ResultModel>> result = Transformations.switchMap(
            Transformations.distinctUntilChanged(shouldPushUpdates),
            input -> {
                if (input) {
                    return dbSource;
                }
                return new MutableLiveData<>(dbSource.getValue());
            }
        );

        paginatedResource =
            new PaginatedResource<>(
                Transformations.distinctUntilChanged(result),
                status,
                boundaryCallback::invalidate,
                boundaryCallback::requestAgain
            );
        boundaryCallback.invalidate();
    }

    public PaginatedNetworkBoundResource(int i, AcmDb localDb) {
        this(i);
        this.localDb = localDb;
    }

    public PaginatedResource<ResultModel> get() {
        return paginatedResource;
    }

    protected abstract Completable saveCallResult(RequestType item);

    protected abstract DataSource.Factory<Integer, ResultModel> loadFromDb();

    protected abstract void createCall(
        MutableLiveData<Resource<RequestType>> reqItem,
        int lastRequestedPage
    );

    protected Completable deleteItems() {
        return Completable.complete();
    }

    protected RequestType processResponse(Resource<RequestType> response) {
        return response.data;
    }

    public class BoundaryCallback
        extends PagedList.BoundaryCallback<ResultModel> {

        private int lastRequestedPage = 1;
        private boolean hasMoreResults = true;
        private final BehaviorSubject<Status> status = BehaviorSubject.createDefault(
            Status.SUCCESS
        );

        @Override
        public void onZeroItemsLoaded() {
            requestAndSaveData();
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull ResultModel itemAtFront) {}

        @Override
        public void onItemAtEndLoaded(@NonNull ResultModel itemAtEnd) {
            requestAndSaveData();
        }

        public Observable<Status> getStatus() {
            return status;
        }

        public void invalidate() {
            lastRequestedPage = 1;
            hasMoreResults = true;
            requestAndSaveData();
        }

        public void requestAgain() {
            requestAndSaveData();
        }

        private void requestAndSaveData() {
            if (status.getValue() == Status.LOADING || !hasMoreResults) return;

            status.onNext(Status.LOADING);
            MutableLiveData<Resource<RequestType>> response = new MutableLiveData<>();
            createCall(response, lastRequestedPage);
            new SingleTimeObserver<Resource<RequestType>>() {
                @Override
                public void onReceived(Resource<RequestType> newData) {
                    switch (newData.status) {
                        case SUCCESS:
                            if (newData.data == null) {
                                hasMoreResults = false;
                                status.onNext(Status.SUCCESS);
                            }

                            shouldPushUpdates.setValue(false);
                            if (lastRequestedPage == 1) {
                                subscribeToCompletable(
                                    Completable.fromAction(
                                        () ->
                                            localDb.runInTransaction(
                                                () ->
                                                    deleteItems()
                                                        .andThen(
                                                            saveCallResult(
                                                                processResponse(
                                                                    newData
                                                                )
                                                            )
                                                        )
                                                        .blockingAwait()
                                            )
                                    )
                                );
                            } else {
                                subscribeToCompletable(
                                    Completable.fromAction(
                                        () ->
                                            localDb
                                                .runInTransaction(
                                                    () ->
                                                        saveCallResult(
                                                            processResponse(
                                                                newData
                                                            )
                                                        )
                                                )
                                                .blockingAwait()
                                    )
                                );
                            }
                            break;
                        case ERROR:
                            Log.d(TAG, "onReceived: " + newData.getMessage());
                            status.onNext(Status.ERROR);
                            break;
                        case NO_DATA:
                            hasMoreResults = false;
                            status.onNext(Status.SUCCESS);
                            break;
                    }
                }
            }
            .attachTo(response);
        }

        @SuppressLint("CheckResult")
        private void subscribeToCompletable(Completable completable) {
            completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> shouldPushUpdates.setValue(true))
                .subscribe(
                    () -> {
                        Log.d(TAG, "onReceived: ");
                        lastRequestedPage++;
                        status.onNext(Status.SUCCESS);
                    }
                );
        }
    }
}

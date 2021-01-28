package com.acmvit.acm_app.util.reactive;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class ReactiveUtils {

    @MainThread
    @NonNull
    public static <X, Y> LiveData<Y> joinLD(
        @NonNull LiveData<X> source1,
        @NonNull LiveData<X> source2,
        @NonNull final Function<X, Y> mapFunction
    ) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(
            source1,
            (Observer<X>) x -> result.setValue(mapFunction.apply(x))
        );
        result.addSource(
            source2,
            (Observer<X>) x -> result.setValue(mapFunction.apply(x))
        );
        return result;
    }
}

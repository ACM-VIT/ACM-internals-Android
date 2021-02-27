package com.acmvit.acm_app.util.reactive;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class SingleSourceMediatorLD<T> extends MediatorLiveData<T> {
    private LiveData<?> source;

    public SingleSourceMediatorLD(T initValue) {
        setValue(initValue);
    }

    public SingleSourceMediatorLD() {
    }

    @Override
    public <S> void addSource(@NonNull LiveData<S> source, @NonNull Observer<? super S> onChanged) {
        if (this.source != null) {
            removeSource(this.source);
        }
        this.source = source;
        super.addSource(source, onChanged);
    }
}

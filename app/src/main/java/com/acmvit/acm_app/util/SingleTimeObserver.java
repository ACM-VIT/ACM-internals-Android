package com.acmvit.acm_app.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public abstract class SingleTimeObserver<T> implements Observer<T> {

    private LiveData<T> liveData;

    public void attachTo(@NonNull LiveData<T> liveData) {
        detachLiveData();
        liveData.observeForever(this);
    }

    public void attachTo(LifecycleOwner owner, @NonNull LiveData<T> liveData) {
        detachLiveData();
        liveData.observe(owner, this);
    }

    public void detachLiveData() {
        if (this.liveData != null) {
            this.liveData.removeObserver(this);
        }
    }

    @Override
    public void onChanged(T t) {
        detachLiveData();
        onReceived(t);
    }

    public abstract void onReceived(T t);
}

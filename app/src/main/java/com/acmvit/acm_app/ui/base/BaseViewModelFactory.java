package com.acmvit.acm_app.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.acmvit.acm_app.ui.ActivityViewModel;

import java.lang.reflect.InvocationTargetException;

public class BaseViewModelFactory implements ViewModelProvider.Factory {

    private final ActivityViewModel activityViewModel;
    private final Application application;

    public BaseViewModelFactory(BaseActivity activity) {
        activityViewModel =
            new ViewModelProvider(
                activity,
                new ViewModelProvider.NewInstanceFactory()
            )
            .get(ActivityViewModel.class);
        application = activity.getApplication();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (BaseViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass
                    .getConstructor(ActivityViewModel.class, Application.class)
                    .newInstance(activityViewModel, application);
            } catch (
                InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e
            ) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

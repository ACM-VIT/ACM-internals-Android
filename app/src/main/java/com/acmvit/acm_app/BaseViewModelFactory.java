package com.acmvit.acm_app;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.acmvit.acm_app.ui.base.ActivityViewModel;
import com.acmvit.acm_app.ui.base.BaseViewModel;

import java.lang.reflect.InvocationTargetException;

public class BaseViewModelFactory extends ViewModelProvider.NewInstanceFactory {
 
    private ActivityViewModel activityViewModel;

    public BaseViewModelFactory(Activity activity) {
        activityViewModel = new ViewModelProvider((ViewModelStoreOwner) activity)
                .get(ActivityViewModel.class);
    }
 
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (BaseViewModel.class.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(BaseViewModel.class).newInstance(activityViewModel);
            } catch (InstantiationException | IllegalAccessException
                    | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return super.create(modelClass);
    }
    
}
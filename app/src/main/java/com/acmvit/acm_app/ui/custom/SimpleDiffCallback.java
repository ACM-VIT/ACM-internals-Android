package com.acmvit.acm_app.ui.custom;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

public abstract class SimpleDiffCallback<T> extends DiffUtil.ItemCallback<T> {

    private static final String TAG = "SimpleDiffCallback";

    public abstract Object getKeyProperty(T t);

    @Override
    public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        if (!getKeyProperty(oldItem).equals(getKeyProperty(newItem))) {}
        return getKeyProperty(oldItem).equals(getKeyProperty(newItem));
    }

    @SuppressLint("DiffUtilEquals")
    @Override
    public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
        return newItem.equals(oldItem);
    }
}

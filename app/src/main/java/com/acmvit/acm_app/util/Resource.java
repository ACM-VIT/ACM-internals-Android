package com.acmvit.acm_app.util;

import static com.acmvit.acm_app.util.Status.ERROR;
import static com.acmvit.acm_app.util.Status.LOADING;
import static com.acmvit.acm_app.util.Status.NO_DATA;
import static com.acmvit.acm_app.util.Status.SUCCESS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    private final String message;

    private Resource(
        @NonNull Status status,
        @Nullable T data,
        @Nullable String message
    ) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }

    public static <T> Resource<T> noData() {
        return new Resource<>(NO_DATA, null, null);
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource<?> resource = (Resource<?>) o;
        return status == resource.status &&
                Objects.equals(data, resource.data) &&
                Objects.equals(message, resource.message);
    }

}

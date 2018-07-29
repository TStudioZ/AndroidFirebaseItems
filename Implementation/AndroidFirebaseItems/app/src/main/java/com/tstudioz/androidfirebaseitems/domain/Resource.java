package com.tstudioz.androidfirebaseitems.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Exception exception;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable Exception exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.success(), data, null);
    }

    public static <T> Resource<T> error(Exception exception, @Nullable T data) {
        return new Resource<>(Status.error(), data, exception);
    }

    public static <T> Resource<T> working(@Nullable T data) {
        return new Resource<>(Status.working(), data, null);
    }
}

package com.tstudioz.androidfirebaseitems.data;

import java.util.List;

public interface IFirebaseDatabaseRepository<Model> {
    void addListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback);

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }
}

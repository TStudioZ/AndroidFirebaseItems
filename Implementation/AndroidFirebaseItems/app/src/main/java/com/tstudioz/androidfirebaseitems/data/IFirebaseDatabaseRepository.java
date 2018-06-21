package com.tstudioz.androidfirebaseitems.data;

import java.util.List;

public interface IFirebaseDatabaseRepository<Model> {
    void addListener(FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeListener();

    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }
}

package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;

import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;

import java.util.List;

public interface IFirebaseDatabaseRepository<Model> {
    void addListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void loadModel(String key, DataItemCallback<Model> callback);
    void save(Model model);
    void delete(Model model);
    LiveData<LiveDataEvent<Resource<Model>>> getSaveModelEvent();
    LiveData<LiveDataEvent<Resource<Model>>> getDeleteModelEvent();

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }

    interface DataItemCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}

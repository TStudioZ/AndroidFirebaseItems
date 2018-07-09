package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;

import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.List;

public interface IFirebaseDatabaseRepository<Model> {
    void addListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void loadModel(String key, DataItemCallback<Model> callback);
    void save(Model model);
    void delete(Model model);
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getSaveModelEvent();
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getDeleteModelEvent();

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }

    interface DataItemCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}

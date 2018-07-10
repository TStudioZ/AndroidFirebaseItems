package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;

import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.List;

public interface IFirebaseDatabaseRepository<Model> {
    void addListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    LiveData<Resource<Model>> loadModel(String key);
    void save(Model modelOld, Model modelNew);
    void delete(Model model);
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getSaveModelEvent();
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getDeleteModelEvent();
    LiveData<Resource<Model>> decreaseCount(Model model);
    LiveData<Resource<Model>> increaseCount(Model model);

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }

    interface DataItemCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}

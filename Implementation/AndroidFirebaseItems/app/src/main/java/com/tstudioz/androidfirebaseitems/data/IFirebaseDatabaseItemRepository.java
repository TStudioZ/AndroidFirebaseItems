package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;

import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.List;

public interface IFirebaseDatabaseItemRepository<Model> {
    void addItemListListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeItemListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    LiveData<Resource<Model>> loadModel(String key);
    void save(Model modelOld, Model modelNew);
    void delete(Model model);
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getSaveModelEvent();
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getUpdateModelEvent();
    LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getDeleteModelEvent();
    LiveData<Resource<Model>> decreaseCount(Model model);
    LiveData<Resource<Model>> increaseCount(Model model);

    LiveData<Resource<Boolean>> isConnected();

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }

    interface DataItemCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}

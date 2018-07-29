package com.tstudioz.androidfirebaseitems.domain.repository;

import android.arch.lifecycle.LiveData;

import com.tstudioz.androidfirebaseitems.domain.Resource;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IFirebaseDatabaseItemRepository<Model> {
    Observable<List<Model>> loadItems();
    Single<Model> loadModel(String key);
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

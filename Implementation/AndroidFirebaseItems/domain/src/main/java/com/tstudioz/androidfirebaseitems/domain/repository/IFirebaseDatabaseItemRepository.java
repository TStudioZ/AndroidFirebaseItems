package com.tstudioz.androidfirebaseitems.domain.repository;

import com.tstudioz.androidfirebaseitems.domain.response.UpdateCountResponse;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IFirebaseDatabaseItemRepository<Model> {
    Observable<List<Model>> loadItems();
    Single<Model> loadModel(String key);
    Single<Model> save(Model modelOld, Model modelNew);
    Single<Model> delete(Model model);
    Observable<Model> getSaveModelEvent();
    Observable<Model> getUpdateModelEvent();
    Observable<Model> getDeleteModelEvent();
    Single<UpdateCountResponse<Model>> decreaseCount(Model model);
    Single<UpdateCountResponse<Model>> increaseCount(Model model);

    Observable<Boolean> isConnected();

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }

    interface DataItemCallback<T> {
        void onSuccess(@Nullable T result);
        void onError(Exception e);
    }
}

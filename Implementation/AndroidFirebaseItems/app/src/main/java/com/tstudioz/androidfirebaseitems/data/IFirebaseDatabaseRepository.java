package com.tstudioz.androidfirebaseitems.data;

import com.tstudioz.essentialuilibrary.viewmodel.SingleLiveEvent;

import java.util.List;

public interface IFirebaseDatabaseRepository<Model> {
    void addListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback);
    void save(Model model);
    SingleLiveEvent<Model> getSaveModelEvent();

    interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);
        void onError(Exception e);
    }
}

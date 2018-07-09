package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;

import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

public interface IFirebaseDatabaseUserRepository {

    void registerLoadUser(String uid, FirebaseUserCallback callback);
    void save(FirebaseUser user);
    LiveData<LiveDataEventWithTaggedObservers<Resource<FirebaseUser>>> getSaveUserEvent();

    interface FirebaseUserCallback {
        void onSuccess(FirebaseUser result);
        void onLoadError(Exception e);
        void onRegisterError(Exception e);
    }
}

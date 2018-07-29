package com.tstudioz.androidfirebaseitems.domain.repository;

import android.arch.lifecycle.LiveData;

import com.tstudioz.androidfirebaseitems.domain.Resource;
import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

public interface IFirebaseDatabaseUserRepository {

    LiveData<Resource<FirebaseUser>> registerLoadUser(String uid);
    void save(FirebaseUser user);
    LiveData<LiveDataEventWithTaggedObservers<Resource<FirebaseUser>>> getSaveUserEvent();

    interface FirebaseUserCallback {
        void onSuccess(FirebaseUser result);
        void onLoadError(Exception e);
        void onRegisterError(Exception e);
    }
}

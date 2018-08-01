package com.tstudioz.androidfirebaseitems.domain.repository;

import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IFirebaseDatabaseUserRepository {

    Single<FirebaseUser> registerLoadUser(String uid);
    Single<FirebaseUser> save(FirebaseUser user);
    Observable<FirebaseUser> getSaveUserEvent();

    interface FirebaseUserCallback {
        void onSuccess(FirebaseUser result);
        void onLoadError(Exception e);
        void onRegisterError(Exception e);
    }
}

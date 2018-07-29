package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;
import com.tstudioz.androidfirebaseitems.domain.Resource;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import javax.inject.Inject;

public class UserViewModel extends ViewModel {

    private IFirebaseDatabaseUserRepository repo;
    private MediatorLiveData<LiveDataEvent<Resource<FirebaseUser>>> user;

    @Inject
    public UserViewModel(final IFirebaseDatabaseUserRepository repo) {
        this.repo = repo;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<FirebaseUser>>> registerLoadUser(String uid) {
        if (user == null) {
            user = new MediatorLiveData<>();
            user.addSource(repo.registerLoadUser(uid), res -> {
                user.setValue(new LiveDataEvent<>(res));
            });
        }
        return user;
    }

    @MainThread
    public void saveUser(FirebaseUser user) {
        repo.save(user);
    }

    @MainThread
    public LiveData<LiveDataEventWithTaggedObservers<Resource<FirebaseUser>>> getSaveUserEvent() {
        return repo.getSaveUserEvent();
    }
}

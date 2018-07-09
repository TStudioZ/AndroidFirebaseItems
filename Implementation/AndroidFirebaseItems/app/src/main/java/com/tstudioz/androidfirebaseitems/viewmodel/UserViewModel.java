package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.R;
import com.tstudioz.androidfirebaseitems.data.FirebaseUser;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseUserRepository;
import com.tstudioz.androidfirebaseitems.data.Resource;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;
import com.tstudioz.essentialuilibrary.viewmodel.SnackbarMessage;

import javax.inject.Inject;

public class UserViewModel extends ViewModel {

    private IFirebaseDatabaseUserRepository repo;
    private MutableLiveData<FirebaseUser> user;
    private SnackbarMessage errorMessage = new SnackbarMessage();

    public SnackbarMessage getErrorMessage() {
        return errorMessage;
    }

    @Inject
    public UserViewModel(final IFirebaseDatabaseUserRepository repo) {
        this.repo = repo;
    }

    @MainThread
    public LiveData<FirebaseUser> registerLoadUser(String uid) {
        if (user == null) {
            user = new MutableLiveData<>();
            repo.registerLoadUser(uid, new IFirebaseDatabaseUserRepository.FirebaseUserCallback() {
                @Override
                public void onSuccess(FirebaseUser result) {
                    user.setValue(result);
                }

                @Override
                public void onLoadError(Exception e) {
                    errorMessage.setValue(R.string.error_loading_user);
                }

                @Override
                public void onRegisterError(Exception e) {
                    errorMessage.setValue(R.string.error_registering_user);
                }
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

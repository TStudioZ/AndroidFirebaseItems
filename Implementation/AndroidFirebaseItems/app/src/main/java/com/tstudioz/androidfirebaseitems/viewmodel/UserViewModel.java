package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.data.Resource;
import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.data.usecase.GetLastSaveUserEventUseCase;
import com.tstudioz.androidfirebaseitems.data.usecase.RegisterLoadUserUseCase;
import com.tstudioz.androidfirebaseitems.data.usecase.SaveUserUseCase;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final RegisterLoadUserUseCase registerLoadUserUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final GetLastSaveUserEventUseCase lastSaveUserEventUseCase;

    private MutableLiveData<LiveDataEvent<Resource<FirebaseUser>>> userLiveData;
    private MutableLiveData<LiveDataEvent<Resource<FirebaseUser>>> saveUserEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEvent<Resource<FirebaseUser>>> lastSaveUserEvent = new MutableLiveData<>();

    @Inject
    public UserViewModel(RegisterLoadUserUseCase registerLoadUserUseCase,
                         SaveUserUseCase saveUserUseCase, GetLastSaveUserEventUseCase lastSaveUserEventUseCase) {
        this.registerLoadUserUseCase = registerLoadUserUseCase;
        this.saveUserUseCase = saveUserUseCase;
        this.lastSaveUserEventUseCase = lastSaveUserEventUseCase;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<FirebaseUser>>> registerLoadUser(String uid) {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
            registerLoadUserUseCase.execute(uid)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> userLiveData.setValue(new LiveDataEvent<>(Resource.working(null))))
                .subscribe(new DisposableSingleObserver<FirebaseUser>() {
                    @Override
                    public void onSuccess(FirebaseUser user) {
                        userLiveData.setValue(new LiveDataEvent<>(Resource.success(user)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        userLiveData.setValue(new LiveDataEvent<>(Resource.error(new Exception(e), null)));
                    }
                });
        }
        return userLiveData;
    }

    @MainThread
    public void saveUser(FirebaseUser user) {
        saveUserUseCase.execute(user)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> saveUserEvent.setValue(new LiveDataEvent<>(Resource.working(null))))
            .subscribe(new DisposableSingleObserver<FirebaseUser>() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    saveUserEvent.setValue(new LiveDataEvent<>(Resource.success(user)));
                }

                @Override
                public void onError(Throwable e) {
                    saveUserEvent.setValue(new LiveDataEvent<>(Resource.error(new Exception(e), null)));
                }
            });
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<FirebaseUser>>> getLastSaveUserEvent() {
        if (lastSaveUserEvent == null) {
            lastSaveUserEvent = new MutableLiveData<>();
            disposables.add(lastSaveUserEventUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
                    lastSaveUserEvent.setValue(new LiveDataEvent<>(Resource.success(user)));
                }, throwable -> {
                    lastSaveUserEvent.setValue(
                            new LiveDataEvent<>(
                                    Resource.error(new Exception(throwable), null)));
                }));
        }
        return lastSaveUserEvent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }
}

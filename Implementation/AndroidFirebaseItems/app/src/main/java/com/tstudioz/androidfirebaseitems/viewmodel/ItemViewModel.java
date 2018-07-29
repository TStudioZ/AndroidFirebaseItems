package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.domain.Resource;
import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemUseCase;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ItemViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final LoadItemUseCase loadItemUseCase;

    private MutableLiveData<LiveDataEvent<Resource<DataItem>>> itemLiveData;

    @Inject
    public ItemViewModel(LoadItemUseCase loadItemUseCase) {
        this.loadItemUseCase = loadItemUseCase;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> loadItem(final String key) {
        if (itemLiveData == null) {
            itemLiveData = new MutableLiveData<>();
            disposables.add(loadItemUseCase.execute(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    itemLiveData.setValue(new LiveDataEvent<>(Resource.working(null)));
                })
                .subscribe(item -> {
                    itemLiveData.setValue(new LiveDataEvent<>(Resource.success(item)));
                }, throwable -> {
                    itemLiveData.setValue(new LiveDataEvent<>(Resource.error(new Exception(throwable), null)));
                }));
        }
        return itemLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }
}

package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tstudioz.androidfirebaseitems.domain.Resource;
import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemsUseCase;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ItemsViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final LoadItemsUseCase loadItemsUseCase;

    private final IFirebaseDatabaseItemRepository<DataItem> repo;

    private MutableLiveData<Resource<List<DataItem>>> itemsLiveData;
    private MediatorLiveData<LiveDataEvent<Resource<DataItem>>> decreaseCountEvent = new MediatorLiveData<>();
    private MediatorLiveData<LiveDataEvent<Resource<DataItem>>> increaseCountEvent = new MediatorLiveData<>();

    @Inject
    public ItemsViewModel(IFirebaseDatabaseItemRepository<DataItem> repo,
                          LoadItemsUseCase loadItemsUseCase) {
        this.repo = repo;
        this.loadItemsUseCase = loadItemsUseCase;
    }

    @MainThread
    public LiveData<Resource<List<DataItem>>> getItems() {
        if (itemsLiveData == null) {
            itemsLiveData = new MutableLiveData<>();
            loadItems();
        }
        return itemsLiveData;
    }

    @MainThread
    private void loadItems() {
        disposables.add(loadItemsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(
                    disposable -> itemsLiveData.setValue(Resource.working(null)))
            .subscribe(items -> {
                itemsLiveData.setValue(Resource.success(items));
            }, throwable -> {
                itemsLiveData.setValue(Resource.error(new Exception(throwable), null));
            }));
    }

    @MainThread
    public void saveItem(@Nullable DataItem oldItem, @NonNull DataItem newItem) {
        repo.save(oldItem, newItem);
    }

    @MainThread
    public void deleteItem(@NonNull DataItem item) {
        repo.delete(item);
    }

    private LiveData<Resource<DataItem>> decreaseCountLiveData = new MutableLiveData<>();
    @MainThread
    public void decreaseCount(@NonNull DataItem item) {
        decreaseCountEvent.removeSource(decreaseCountLiveData);
        decreaseCountLiveData = repo.decreaseCount(item);
        decreaseCountEvent.addSource(decreaseCountLiveData, res -> {
            decreaseCountEvent.setValue(new LiveDataEvent<>(res));
        });
    }

    private LiveData<Resource<DataItem>> increaseCountLiveData = new MutableLiveData<>();
    @MainThread
    public void increaseCount(@NonNull DataItem item) {
        increaseCountEvent.removeSource(increaseCountLiveData);
        increaseCountLiveData = repo.increaseCount(item);
        increaseCountEvent.addSource(increaseCountLiveData, res -> {
            increaseCountEvent.setValue(new LiveDataEvent<>(res));
        });
    }

    @MainThread
    public LiveData<LiveDataEventWithTaggedObservers<Resource<DataItem>>> getSaveItemEvent() {
        return repo.getSaveModelEvent();
    }

    @MainThread
    public LiveData<LiveDataEventWithTaggedObservers<Resource<DataItem>>> getUpdateItemEvent() {
        return repo.getUpdateModelEvent();
    }

    @MainThread
    public LiveData<LiveDataEventWithTaggedObservers<Resource<DataItem>>> getDeleteItemEvent() {
        return repo.getDeleteModelEvent();
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getDecreaseCountEvent() {
        return decreaseCountEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getIncreaseCountEvent() {
        return increaseCountEvent;
    }

    @MainThread
    public LiveData<Resource<Boolean>> isConnected() {
        return repo.isConnected();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }
}

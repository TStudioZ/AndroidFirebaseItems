package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tstudioz.androidfirebaseitems.domain.Resource;
import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.response.UpdateCountResponse;
import com.tstudioz.androidfirebaseitems.domain.usecase.DecreaseCountUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.DeleteItemUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.GetLastDeleteItemEventUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.GetLastSaveItemEventUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.GetLastUpdateItemEventUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.IncreaseCountUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.IsConnectedUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemsUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.SaveItemUseCase;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ItemsViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final LoadItemsUseCase loadItemsUseCase;
    private final SaveItemUseCase saveItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;
    private final DecreaseCountUseCase decreaseCountUseCase;
    private final IncreaseCountUseCase increaseCountUseCase;
    private final GetLastSaveItemEventUseCase getLastSaveItemEventUseCase;
    private final GetLastUpdateItemEventUseCase getLastUpdateItemEventUseCase;
    private final GetLastDeleteItemEventUseCase getLastDeleteItemEventUseCase;
    private final IsConnectedUseCase isConnectedUseCase;

    private MutableLiveData<Resource<List<DataItem>>> itemsLiveData;
    private MutableLiveData<LiveDataEvent<Resource<DataItem>>> saveItemEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEvent<Resource<DataItem>>> deleteItemEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEvent<Resource<DataItem>>> lastSaveItemEvent;
    private MutableLiveData<LiveDataEvent<Resource<DataItem>>> lastUpdateItemEvent;
    private MutableLiveData<LiveDataEvent<Resource<DataItem>>> lastDeleteItemEvent;
    private MutableLiveData<LiveDataEvent<Resource<UpdateCountResponse<DataItem>>>> decreaseCountEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEvent<Resource<UpdateCountResponse<DataItem>>>> increaseCountEvent = new MutableLiveData<>();
    private MutableLiveData<Resource<Boolean>> isConnectedLiveData = new MutableLiveData<>();

    @Inject
    public ItemsViewModel(LoadItemsUseCase loadItemsUseCase,
                          SaveItemUseCase saveItemUseCase,
                          DeleteItemUseCase deleteItemUseCase,
                          DecreaseCountUseCase decreaseCountUseCase,
                          IncreaseCountUseCase increaseCountUseCase,
                          GetLastSaveItemEventUseCase getLastSaveItemEventUseCase,
                          GetLastUpdateItemEventUseCase getLastUpdateItemEventUseCase,
                          GetLastDeleteItemEventUseCase getLastDeleteItemEventUseCase,
                          IsConnectedUseCase isConnectedUseCase) {
        this.loadItemsUseCase = loadItemsUseCase;
        this.saveItemUseCase = saveItemUseCase;
        this.deleteItemUseCase = deleteItemUseCase;
        this.decreaseCountUseCase = decreaseCountUseCase;
        this.increaseCountUseCase = increaseCountUseCase;
        this.getLastSaveItemEventUseCase = getLastSaveItemEventUseCase;
        this.getLastUpdateItemEventUseCase = getLastUpdateItemEventUseCase;
        this.getLastDeleteItemEventUseCase = getLastDeleteItemEventUseCase;
        this.isConnectedUseCase = isConnectedUseCase;
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
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> itemsLiveData.setValue(Resource.working(null)))
            .subscribe(items -> {
                itemsLiveData.setValue(Resource.success(items));
            }, throwable -> {
                itemsLiveData.setValue(Resource.error(new Exception(throwable), null));
            }));
    }

    @MainThread
    public void saveItem(@Nullable DataItem oldItem, @NonNull DataItem newItem) {
        saveItemUseCase.execute(oldItem, newItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> saveItemEvent.setValue(new LiveDataEvent<>(Resource.working(null))))
            .subscribe(new DisposableSingleObserver<DataItem>() {
                @Override
                public void onSuccess(DataItem item) {
                    saveItemEvent.setValue(new LiveDataEvent<>(Resource.success(item)));
                }

                @Override
                public void onError(Throwable e) {
                    saveItemEvent.setValue(new LiveDataEvent<>(Resource.error(new Exception(e), null)));
                }
            });
    }

    @MainThread
    public void deleteItem(@NonNull DataItem item) {
        deleteItemUseCase.execute(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> deleteItemEvent.setValue(new LiveDataEvent<>(Resource.working(null))))
            .subscribe(new DisposableSingleObserver<DataItem>() {
                @Override
                public void onSuccess(DataItem item) {
                    deleteItemEvent.setValue(new LiveDataEvent<>(Resource.success(item)));
                }

                @Override
                public void onError(Throwable e) {
                    deleteItemEvent.setValue(new LiveDataEvent<>(Resource.error(new Exception(e), null)));
                }
            });
    }

    @MainThread
    public void decreaseCount(@NonNull DataItem item) {
        decreaseCountUseCase.execute(item)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> decreaseCountEvent.setValue(
                    new LiveDataEvent<>(Resource.working(new UpdateCountResponse<>(item, false)))))
            .subscribe(new DisposableSingleObserver<UpdateCountResponse<DataItem>>() {
                @Override
                public void onSuccess(UpdateCountResponse<DataItem> result) {
                    decreaseCountEvent.setValue(new LiveDataEvent<>(Resource.success(result)));
                }

                @Override
                public void onError(Throwable e) {
                    decreaseCountEvent.setValue(new LiveDataEvent<>(
                            Resource.error(new Exception(e), new UpdateCountResponse<>(item, false))));
                }
            });
    }

    @MainThread
    public void increaseCount(@NonNull DataItem item) {
        increaseCountUseCase.execute(item)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> increaseCountEvent.setValue(
                    new LiveDataEvent<>(Resource.working(new UpdateCountResponse<>(item, false)))))
            .subscribe(new DisposableSingleObserver<UpdateCountResponse<DataItem>>() {
                @Override
                public void onSuccess(UpdateCountResponse<DataItem> result) {
                    increaseCountEvent.setValue(new LiveDataEvent<>(Resource.success(result)));
                }

                @Override
                public void onError(Throwable e) {
                    increaseCountEvent.setValue(new LiveDataEvent<>(
                            Resource.error(new Exception(e), new UpdateCountResponse<>(item, false))));
                }
            });
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getSaveItemEvent() {
        return saveItemEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getDeleteItemEvent() {
        return deleteItemEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getLastSaveItemEvent() {
        if (lastSaveItemEvent == null) {
            lastSaveItemEvent = new MutableLiveData<>();
            disposables.add(getLastSaveItemEventUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemNew -> {
                    lastSaveItemEvent.setValue(new LiveDataEvent<>(Resource.success(itemNew)));
                }, throwable -> lastSaveItemEvent.setValue(
                        new LiveDataEvent<>(
                                Resource.error(new Exception(throwable), null)))));
        }
        return lastSaveItemEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getLastUpdateItemEvent() {
        if (lastUpdateItemEvent == null) {
            lastUpdateItemEvent = new MutableLiveData<>();
            disposables.add(getLastUpdateItemEventUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemOld -> {
                    lastUpdateItemEvent.setValue(new LiveDataEvent<>(Resource.success(itemOld)));
                }, throwable -> lastUpdateItemEvent.setValue(
                        new LiveDataEvent<>(
                                Resource.error(new Exception(throwable), null)))));
        }
        return lastUpdateItemEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> getLastDeleteItemEvent() {
        if (lastDeleteItemEvent == null) {
            lastDeleteItemEvent = new MutableLiveData<>();
            disposables.add(getLastDeleteItemEventUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    lastDeleteItemEvent.setValue(new LiveDataEvent<>(Resource.success(item)));
                }, throwable -> lastDeleteItemEvent.setValue(
                        new LiveDataEvent<>(
                                Resource.error(new Exception(throwable), null)))));
        }
        return lastDeleteItemEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<UpdateCountResponse<DataItem>>>> getDecreaseCountEvent() {
        return decreaseCountEvent;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<UpdateCountResponse<DataItem>>>> getIncreaseCountEvent() {
        return increaseCountEvent;
    }

    @MainThread
    public LiveData<Resource<Boolean>> isConnected() {
        if (isConnectedLiveData == null) {
            isConnectedLiveData = new MutableLiveData<>();
            disposables.add(isConnectedUseCase.execute()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isConnectedLiveData.setValue(Resource.working(null)))
                .subscribe(connected -> {
                    isConnectedLiveData.setValue(Resource.success(connected));
                }, throwable -> {
                    isConnectedLiveData.setValue(Resource.error(new Exception(throwable), null));
                }));
        }
        return isConnectedLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }
}

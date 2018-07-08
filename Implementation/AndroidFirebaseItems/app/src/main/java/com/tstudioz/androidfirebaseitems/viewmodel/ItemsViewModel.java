package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseRepository;
import com.tstudioz.essentialuilibrary.viewmodel.SingleLiveEvent;
import com.tstudioz.essentialuilibrary.viewmodel.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;

public class ItemsViewModel extends ViewModel {

    private IFirebaseDatabaseRepository<DataItem> repo;

    private MutableLiveData<List<DataItem>> items;
    private IFirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<DataItem> callback;

    private SnackbarMessage itemAddedMessage = new SnackbarMessage();

    public SnackbarMessage getItemAddedMessage() {
        return itemAddedMessage;
    }

    @Inject
    public ItemsViewModel(final IFirebaseDatabaseRepository<DataItem> repo) {
        this.repo = repo;
        this.callback = new IFirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<DataItem>() {
            @Override
            public void onSuccess(List<DataItem> result) {
                items.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                items.setValue(null);
            }
        };
    }

    @MainThread
    public LiveData<List<DataItem>> getItems() {
        if (items == null) {
            items = new MutableLiveData<>();
            loadItems();
        }
        return items;
    }

    private void loadItems() {
        repo.addListener(callback);
    }

    public void saveItem(DataItem item) {
        repo.save(item);
    }

    public SingleLiveEvent<DataItem> getSaveItemEvent() {
        return repo.getSaveModelEvent();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repo.removeListener(callback);
    }
}

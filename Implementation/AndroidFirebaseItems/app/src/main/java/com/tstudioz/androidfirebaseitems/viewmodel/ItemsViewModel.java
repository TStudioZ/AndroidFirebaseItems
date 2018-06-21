package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseRepository;

import java.util.List;

import javax.inject.Inject;

public class ItemsViewModel extends ViewModel {

    private MutableLiveData<List<DataItem>> items;
    private IFirebaseDatabaseRepository<DataItem> repo;

    @Inject
    public ItemsViewModel(final IFirebaseDatabaseRepository<DataItem> repo) {
        this.repo = repo;
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
        repo.addListener(new IFirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<DataItem>() {
            @Override
            public void onSuccess(List<DataItem> result) {
                items.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                items.setValue(null);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repo.removeListener();
    }
}

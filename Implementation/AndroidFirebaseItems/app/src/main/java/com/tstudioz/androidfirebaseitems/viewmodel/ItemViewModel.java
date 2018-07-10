package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseRepository;
import com.tstudioz.androidfirebaseitems.data.Resource;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;

import javax.inject.Inject;

public class ItemViewModel extends ViewModel {

    private IFirebaseDatabaseRepository<DataItem> repo;
    private MediatorLiveData<LiveDataEvent<Resource<DataItem>>> dataItem;

    @Inject
    public ItemViewModel(final IFirebaseDatabaseRepository<DataItem> repo) {
        this.repo = repo;
    }

    @MainThread
    public LiveData<LiveDataEvent<Resource<DataItem>>> loadItem(String key) {
        if (dataItem == null) {
            dataItem = new MediatorLiveData<>();
            dataItem.addSource(repo.loadModel(key), res -> {
                dataItem.setValue(new LiveDataEvent<>(res));
            });
        }
        return dataItem;
    }
}

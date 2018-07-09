package com.tstudioz.androidfirebaseitems.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.tstudioz.androidfirebaseitems.R;
import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseRepository;
import com.tstudioz.essentialuilibrary.viewmodel.SingleLiveEvent;
import com.tstudioz.essentialuilibrary.viewmodel.SnackbarMessage;

import javax.inject.Inject;

public class ItemViewModel extends ViewModel {

    private IFirebaseDatabaseRepository<DataItem> repo;
    private SingleLiveEvent<DataItem> dataItem;
    private SnackbarMessage errorMessage = new SnackbarMessage();

    public SnackbarMessage getErrorMessage() {
        return errorMessage;
    }

    @Inject
    public ItemViewModel(final IFirebaseDatabaseRepository<DataItem> repo) {
        this.repo = repo;
    }

    @MainThread
    public SingleLiveEvent<DataItem> loadItem(String key) {
        if (dataItem == null) {
            dataItem = new SingleLiveEvent<>();
            repo.loadModel(key, new IFirebaseDatabaseRepository.DataItemCallback<DataItem>() {
                @Override
                public void onSuccess(DataItem item) {
                    dataItem.setValue(item);
                }

                @Override
                public void onError(Exception e) {
                    errorMessage.setValue(R.string.error_loading_item);
                }
            });
        }
        return dataItem;
    }
}

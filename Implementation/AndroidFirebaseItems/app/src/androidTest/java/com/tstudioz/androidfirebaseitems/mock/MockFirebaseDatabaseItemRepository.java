package com.tstudioz.androidfirebaseitems.mock;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.data.Resource;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.HashMap;
import java.util.Map;

public class MockFirebaseDatabaseItemRepository implements IFirebaseDatabaseItemRepository<DataItem> {

    public static final String KEY_ITEM_01 = "item_01";
    private static Map<String, DataItem> mockItems;

    static {
        mockItems = new HashMap<>();
        mockItems.put(KEY_ITEM_01, new DataItem(KEY_ITEM_01, "Item 1", 1));
    }

    @Override
    public void addItemListListener(FirebaseDatabaseRepositoryCallback<DataItem> callback) {

    }

    @Override
    public void removeItemListener(FirebaseDatabaseRepositoryCallback<DataItem> callback) {

    }

    @Override
    public LiveData<Resource<DataItem>> loadModel(String key) {
        MutableLiveData<Resource<DataItem>> res = new MutableLiveData<>();
        res.setValue(Resource.working(null));

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            DataItem item = mockItems.get(key);
            res.setValue(Resource.success(item));
        }, 100);

        return res;
    }

    @Override
    public void save(DataItem modelOld, DataItem modelNew) {

    }

    @Override
    public void delete(DataItem item) {

    }

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<DataItem>>> getSaveModelEvent() {
        return null;
    }

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<DataItem>>> getUpdateModelEvent() {
        return null;
    }

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<DataItem>>> getDeleteModelEvent() {
        return null;
    }

    @Override
    public LiveData<Resource<DataItem>> decreaseCount(DataItem item) {
        return null;
    }

    @Override
    public LiveData<Resource<DataItem>> increaseCount(DataItem item) {
        return null;
    }

    @Override
    public LiveData<Resource<Boolean>> isConnected() {
        return null;
    }
}

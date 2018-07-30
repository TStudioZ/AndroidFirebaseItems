package com.tstudioz.androidfirebaseitems.mock;

import android.os.Handler;
import android.os.Looper;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.response.UpdateCountResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class MockFirebaseDatabaseItemRepository implements IFirebaseDatabaseItemRepository<DataItem> {

    public static final String KEY_ITEM_01 = "item_01";
    private static Map<String, DataItem> mockItems;

    static {
        mockItems = new HashMap<>();
        mockItems.put(KEY_ITEM_01, new DataItem(KEY_ITEM_01, "Item 1", 1));
    }

    @Override
    public Observable<List<DataItem>> loadItems() {
        return null;
    }

    @Override
    public Single<DataItem> loadModel(String key) {
        DataItem item = mockItems.get(key);
        return Single.create(new SingleOnSubscribe<DataItem>() {
            @Override
            public void subscribe(SingleEmitter<DataItem> emitter) throws Exception {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    emitter.onSuccess(item);
                }, 100);
            }
        });
    }

    @Override
    public Single<DataItem> save(DataItem modelOld, DataItem modelNew) {
        return null;
    }

    @Override
    public Single<DataItem> delete(DataItem dataItem) {
        return null;
    }

    @Override
    public Observable<DataItem> getSaveModelEvent() {
        return null;
    }

    @Override
    public Observable<DataItem> getUpdateModelEvent() {
        return null;
    }

    @Override
    public Observable<DataItem> getDeleteModelEvent() {
        return null;
    }

    @Override
    public Single<UpdateCountResponse<DataItem>> decreaseCount(DataItem dataItem) {
        return null;
    }

    @Override
    public Single<UpdateCountResponse<DataItem>> increaseCount(DataItem dataItem) {
        return null;
    }

    @Override
    public Observable<Boolean> isConnected() {
        return null;
    }
}

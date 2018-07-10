package com.tstudioz.androidfirebaseitems.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class DataItemRepository extends FirebaseDatabaseRepository<DataItem, DataItemEntity> {

    private static final int MAX_ITEM_COUNT = 999;
    private static final String ITEM_KEY = "itemKey";
    private static final String TIME = "time";
    private static final String NAME = "name";
    private static final String COUNT = "count";
    private static final String ITEM = "item";
    private static final String OLD_NAME = "oldName";
    private static final String OLD_COUNT = "oldCount";
    private static final String ITEM_OLD = "itemOld";
    private static final String NEW_NAME = "newName";
    private static final String NEW_COUNT = "newCount";
    private static final String ITEM_NEW = "itemNew";

    public DataItemRepository() {
        super(new DataItemMapper());
    }

    @Override
    protected String getRootNode() {
        return "items";
    }

    @Override
    protected String getModelsNode() {
        return "items";
    }

    @Override
    protected String getEventsNode() {
        return "events";
    }

    @Override
    protected String getModelKey(DataItem dataItem) {
        return dataItem.getKey();
    }

    @Override
    protected void setModelKey(DataItem dataItem, String key) {
        dataItem.setKey(key);
    }

    @Override
    protected DataItem cloneModel(DataItem item) {
        return new DataItem(item.getKey(), item.getName(), item.getCount());
    }

    @Override
    protected void updateCountImpl(boolean increase, DatabaseReference ref, DataItem item, DataItemCallback<DataItem> callback) {
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                DataItemEntity i = mutableData.getValue(mapper.getEntityClass());
                if (i == null) {
                    return Transaction.success(mutableData);
                }

                if (increase) {
                    if (i.getCount() < MAX_ITEM_COUNT) {
                        i.setCount(i.getCount() + 1);
                        mutableData.setValue(i);
                    } else {
                        return Transaction.abort();
                    }
                } else {
                    if (i.getCount() <= 1) {
                        mutableData.setValue(null);
                    } else {
                        i.setCount(i.getCount() - 1);
                        mutableData.setValue(i);
                    }
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean commited, @Nullable DataSnapshot dataSnapshot) {
                Log.d("DataItemRepository", "Transaction decreaseCountImpl:onComplete, error: " + databaseError);

                if (commited) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(databaseError != null ? databaseError.toException() : null);
                }
            }
        });
    }

    @Override
    protected void saveSaveItemEvent(String itemKey, DataItem item) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(ITEM_KEY, itemKey);
        payload.put(TIME, ServerValue.TIMESTAMP);
        Map<String, Object> itemPayload = new HashMap<>();
        itemPayload.put(NAME, item.getName());
        itemPayload.put(COUNT, item.getCount());
        payload.put(ITEM, itemPayload);
        saveEvent(EVENT_SAVE_ITEM, payload);
    }

    @Override
    protected void saveEditItemEvent(String itemKey, DataItem itemOld, DataItem itemNew) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(ITEM_KEY, itemKey);
        payload.put(TIME, ServerValue.TIMESTAMP);
        Map<String, Object> itemOldPayload = new HashMap<>();
        itemOldPayload.put(OLD_NAME, itemOld.getName());
        itemOldPayload.put(OLD_COUNT, itemOld.getCount());
        payload.put(ITEM_OLD, itemOldPayload);
        Map<String, Object> itemNewPayload = new HashMap<>();
        itemNewPayload.put(NEW_NAME, itemNew.getName());
        itemNewPayload.put(NEW_COUNT, itemNew.getCount());
        payload.put(ITEM_NEW, itemNewPayload);
        saveEvent(EVENT_EDIT_ITEM, payload);
    }

    @Override
    protected void saveDeleteItemEvent(String itemKey, DataItem item) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(ITEM_KEY, itemKey);
        payload.put(TIME, ServerValue.TIMESTAMP);
        Map<String, Object> itemPayload = new HashMap<>();
        itemPayload.put(NAME, item.getName());
        itemPayload.put(COUNT, item.getCount());
        payload.put(ITEM, itemPayload);
        saveEvent(EVENT_DELETE_ITEM, payload);
    }

    @Override
    protected void saveDecreaseCountEvent(String itemKey, DataItem item) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(ITEM_KEY, itemKey);
        payload.put(TIME, ServerValue.TIMESTAMP);
        payload.put(OLD_COUNT, item.getCount());
        saveEvent(EVENT_DECREASE_COUNT, payload);
    }

    @Override
    protected void saveIncreaseCountEvent(String itemKey, DataItem item) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(ITEM_KEY, itemKey);
        payload.put(TIME, ServerValue.TIMESTAMP);
        payload.put(OLD_COUNT, item.getCount());
        saveEvent(EVENT_INCREASE_COUNT, payload);
    }

    @Override
    protected void saveEvent(String eventName, Map<String, Object> payload) {
        FirebaseEventEntity event = new FirebaseEventEntity(eventName, getUserUID(), payload);
        DatabaseReference ref = dbReference.child(getEventsNode()).push();
        ref.setValue(event);
    }
}

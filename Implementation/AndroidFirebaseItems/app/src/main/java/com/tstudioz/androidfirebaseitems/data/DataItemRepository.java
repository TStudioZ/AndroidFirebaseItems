package com.tstudioz.androidfirebaseitems.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import javax.inject.Singleton;

@Singleton
public class DataItemRepository extends FirebaseDatabaseRepository<DataItem, DataItemEntity> {

    private static final int MAX_ITEM_COUNT = 999;

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
                Log.d("DataItemRepository", "Transaction decreaseCountImpl:onComplete" + databaseError);

                if (commited) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(databaseError != null ? databaseError.toException() : null);
                }
            }
        });
    }
}

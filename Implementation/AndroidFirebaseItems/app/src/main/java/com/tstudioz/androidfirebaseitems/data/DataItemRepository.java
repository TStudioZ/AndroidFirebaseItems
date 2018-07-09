package com.tstudioz.androidfirebaseitems.data;

import javax.inject.Singleton;

@Singleton
public class DataItemRepository extends FirebaseDatabaseRepository<DataItem, DataItemEntity> {

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
}

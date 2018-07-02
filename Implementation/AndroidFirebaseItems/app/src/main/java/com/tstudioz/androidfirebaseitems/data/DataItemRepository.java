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
}

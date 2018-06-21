package com.tstudioz.androidfirebaseitems.data;

public class DataItemMapper extends FirebaseMapper<DataItemEntity, DataItem> {

    @Override
    public DataItem map(DataItemEntity e) {
        return new DataItem(e.getName());
    }
}

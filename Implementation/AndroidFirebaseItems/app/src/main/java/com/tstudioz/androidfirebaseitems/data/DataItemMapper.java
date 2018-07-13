package com.tstudioz.androidfirebaseitems.data;

public class DataItemMapper extends FirebaseItemMapper<DataItemEntity, DataItem> {

    @Override
    public DataItemEntity mapToSource(DataItem dataItem) {
        return new DataItemEntity(dataItem.getName(), dataItem.getCount());
    }

    @Override
    public DataItem mapToDestination(String key, DataItemEntity dataItemEntity) {
        return new DataItem(key, dataItemEntity.getName(), dataItemEntity.getCount());
    }
}

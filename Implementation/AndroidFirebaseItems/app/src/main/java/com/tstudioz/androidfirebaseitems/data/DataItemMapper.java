package com.tstudioz.androidfirebaseitems.data;

public class DataItemMapper extends FirebaseMapper<DataItemEntity, DataItem> {

    @Override
    public DataItemEntity mapToSource(DataItem dataItem) {
        return new DataItemEntity(dataItem.getName(), dataItem.getCount());
    }

    @Override
    public DataItem mapToDestination(DataItemEntity dataItemEntity) {
        return new DataItem(dataItemEntity.getName(), dataItemEntity.getCount());
    }
}

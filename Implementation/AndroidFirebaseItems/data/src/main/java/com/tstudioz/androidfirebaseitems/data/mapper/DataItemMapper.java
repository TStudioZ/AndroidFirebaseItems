package com.tstudioz.androidfirebaseitems.data.mapper;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.data.model.DataItemEntity;

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

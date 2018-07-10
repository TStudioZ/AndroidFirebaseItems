package com.tstudioz.androidfirebaseitems.data;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseMapper<Entity, Model> implements IMapper<Entity, Model> {

    public Model map(String key, DataSnapshot dataSnapshot) {
        Entity entity = dataSnapshot.getValue(getEntityClass());
        return mapToDestination(key, entity);
    }

    public List<Model> mapList(DataSnapshot dataSnapshot) {
        List<Model> list = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            list.add(map(item.getKey(), item));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public Class<Entity> getEntityClass() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<Entity>) superclass.getActualTypeArguments()[0];
    }
}

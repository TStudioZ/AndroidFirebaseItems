package com.tstudioz.androidfirebaseitems.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseDatabaseRepository<Model, Entity> implements IFirebaseDatabaseRepository<Model> {

    private final DatabaseReference dbReference;
    private final FirebaseMapper<Entity, Model> mapper;
    private Map<FirebaseDatabaseRepositoryCallback<Model>, BaseValueEventListener<Model, Entity>> listenerMap;

    protected abstract String getRootNode();

    public FirebaseDatabaseRepository(FirebaseMapper<Entity, Model> mapper) {
        this.dbReference = FirebaseDatabase.getInstance().getReference(getRootNode());
        this.mapper = mapper;
        this.listenerMap = new HashMap<>();
    }

    @Override
    public void addListener(FirebaseDatabaseRepositoryCallback<Model> callback) {
        BaseValueEventListener<Model, Entity> listener = new BaseValueEventListener<>(mapper, callback);
        listenerMap.put(callback, listener);
        dbReference.addValueEventListener(listener);
    }

    @Override
    public void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback) {
        if (listenerMap.containsKey(callback)) {
            BaseValueEventListener<Model, Entity> listener = listenerMap.remove(callback);
            dbReference.removeEventListener(listener);
        }
    }
}

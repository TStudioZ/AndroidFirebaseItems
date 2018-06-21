package com.tstudioz.androidfirebaseitems.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FirebaseDatabaseRepository<Model> implements IFirebaseDatabaseRepository<Model> {

    protected final DatabaseReference dbReference;
    private final FirebaseMapper mapper;
    protected FirebaseDatabaseRepositoryCallback<Model> callback;
    private BaseValueEventListener listener;

    protected abstract String getRootNode();

    public FirebaseDatabaseRepository(FirebaseMapper mapper) {
        this.dbReference = FirebaseDatabase.getInstance().getReference(getRootNode());
        this.mapper = mapper;
    }

    @Override
    public void addListener(FirebaseDatabaseRepositoryCallback<Model> callback) {
        this.callback = callback;
        this.listener = new BaseValueEventListener<>(mapper, callback);
        dbReference.addValueEventListener(listener);
    }

    @Override
    public void removeListener() {
        dbReference.removeEventListener(listener);
    }
}

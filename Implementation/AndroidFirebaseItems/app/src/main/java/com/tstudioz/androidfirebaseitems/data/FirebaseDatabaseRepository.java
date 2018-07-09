package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseDatabaseRepository<Model, Entity> implements IFirebaseDatabaseRepository<Model> {

    private final DatabaseReference dbReference;
    private final FirebaseMapper<Entity, Model> mapper;
    private Map<FirebaseDatabaseRepositoryCallback<Model>, BaseValueEventListener<Model, Entity>> listenerMap;

    private MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> saveModelEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> deleteModelEvent = new MutableLiveData<>();

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getSaveModelEvent() {
        return saveModelEvent;
    }

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getDeleteModelEvent() {
        return deleteModelEvent;
    }

    protected abstract String getRootNode();
    protected abstract String getModelsNode();
    protected abstract String getModelKey(Model model);
    protected abstract void setModelKey(Model model, String key);

    public FirebaseDatabaseRepository(FirebaseMapper<Entity, Model> mapper) {
        this.dbReference = FirebaseDatabase.getInstance().getReference();
        this.mapper = mapper;
        this.listenerMap = new HashMap<>();
    }

    @Override
    public void addListener(FirebaseDatabaseRepositoryCallback<Model> callback) {
        BaseValueEventListener<Model, Entity> listener = new BaseValueEventListener<>(mapper, callback);
        listenerMap.put(callback, listener);
        dbReference.child(getModelsNode()).addValueEventListener(listener);
    }

    @Override
    public void removeListener(FirebaseDatabaseRepositoryCallback<Model> callback) {
        if (listenerMap.containsKey(callback)) {
            BaseValueEventListener<Model, Entity> listener = listenerMap.remove(callback);
            dbReference.child(getModelsNode()).removeEventListener(listener);
        }
    }

    @Override
    public void loadModel(String key, DataItemCallback<Model> callback) {
        DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Model model = mapper.map(key, dataSnapshot);
                callback.onSuccess(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.toException());
            }
        });
    }

    @Override
    public void save(Model model) {
        Entity entity = mapper.mapToSource(model);
        DatabaseReference ref;
        String key = getModelKey(model);
        if (key == null) {
            ref = dbReference.child(getModelsNode()).push();
        } else {
            ref = dbReference.child(getModelsNode()).child(key);
        }
        listenForResult(ref, model, ref.setValue(entity), saveModelEvent);
    }

    @Override
    public void delete(Model model) {
        String key = getModelKey(model);
        if (key == null) return;

        DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
        listenForResult(ref, model, ref.removeValue(), deleteModelEvent);
    }

    private <T> void listenForResult(DatabaseReference ref,
                                     Model model,
                                     Task<T> task,
                                     MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> event) {
        task.addOnSuccessListener(aVoid -> {
            setModelKey(model, ref.getKey());
            event.setValue(new LiveDataEventWithTaggedObservers<>(Resource.success(model)));
        }).addOnFailureListener(e -> {
            event.setValue(new LiveDataEventWithTaggedObservers<>(Resource.error(e, model)));
        });
    }
}

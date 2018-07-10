package com.tstudioz.androidfirebaseitems.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEventWithTaggedObservers;

import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseDatabaseRepository<Model, Entity> implements IFirebaseDatabaseRepository<Model> {

    protected final DatabaseReference dbReference;
    protected final FirebaseMapper<Entity, Model> mapper;
    private Map<FirebaseDatabaseRepositoryCallback<Model>, BaseValueEventListener<Model, Entity>> listenerMap;

    private MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> saveModelEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> updateModelEvent = new MutableLiveData<>();
    private MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> deleteModelEvent = new MutableLiveData<>();

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getSaveModelEvent() {
        return saveModelEvent;
    }

    @Override
    public MutableLiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getUpdateModelEvent() {
        return updateModelEvent;
    }

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<Model>>> getDeleteModelEvent() {
        return deleteModelEvent;
    }

    protected abstract String getRootNode();
    protected abstract String getModelsNode();
    protected abstract String getEventsNode();
    protected abstract String getModelKey(Model model);
    protected abstract void setModelKey(Model model, String key);
    protected abstract Model cloneModel(Model model);
    protected abstract void updateCountImpl(boolean increase, DatabaseReference ref, Model model, DataItemCallback<Model> callback);

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
    public LiveData<Resource<Model>> loadModel(String key) {
        MutableLiveData<Resource<Model>> res = new MutableLiveData<>();
        res.setValue(Resource.working(null));

        DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Model model = mapper.map(key, dataSnapshot);
                res.setValue(Resource.success(model));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                res.setValue(Resource.error(databaseError.toException(), null));
            }
        });
        return res;
    }

    @Override
    public void save(Model modelOld, Model modelNew) {
        Entity entity = mapper.mapToSource(modelNew);
        DatabaseReference ref;
        String key = getModelKey(modelNew);
        if (key == null) {
            ref = dbReference.child(getModelsNode()).push();
            ref.setValue(entity).addOnSuccessListener(aVoid -> {
                saveSaveItemEvent(ref.getKey(), modelNew);
                setModelKey(modelNew, ref.getKey());
                saveModelEvent.setValue(new LiveDataEventWithTaggedObservers<>(Resource.success(modelNew)));
            }).addOnFailureListener(e -> {
                saveModelEvent.setValue(new LiveDataEventWithTaggedObservers<>(Resource.error(e, modelNew)));
            });
        } else {
            ref = dbReference.child(getModelsNode()).child(key);
            ref.setValue(entity).addOnSuccessListener(aVoid -> {
                saveEditItemEvent(ref.getKey(), modelOld, modelNew);
                setModelKey(modelNew, ref.getKey());
                updateModelEvent.setValue(new LiveDataEventWithTaggedObservers<>(Resource.success(modelOld)));
            }).addOnFailureListener(e -> {
                updateModelEvent.setValue(new LiveDataEventWithTaggedObservers<>(Resource.error(e, modelOld)));
            });
        }
    }

    @Override
    public void delete(Model model) {
        String key = getModelKey(model);
        if (key == null) return;

        DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
        ref.removeValue().addOnSuccessListener(aVoid -> {
            saveDeleteItemEvent(ref.getKey(), model);
            setModelKey(model, ref.getKey());
            deleteModelEvent.setValue(new LiveDataEventWithTaggedObservers<>(Resource.success(model)));
        }).addOnFailureListener(e -> {
            deleteModelEvent.setValue(new LiveDataEventWithTaggedObservers<>(Resource.error(e, model)));
        });
    }

    @Override
    public LiveData<Resource<Model>> decreaseCount(Model model) {
        return updateCount(model, false);
    }

    @Override
    public LiveData<Resource<Model>> increaseCount(Model model) {
        return updateCount(model, true);
    }

    private LiveData<Resource<Model>> updateCount(Model model, boolean increase) {
        MutableLiveData<Resource<Model>> res = new MutableLiveData<>();
        res.setValue(Resource.working(null));

        Model cloned = cloneModel(model);
        String key = getModelKey(model);

        DataItemCallback<Model> callback = new DataItemCallback<Model>() {
            @Override
            public void onSuccess(Model result) {
                res.setValue(Resource.success(result));
            }

            @Override
            public void onError(Exception e) {
                res.setValue(Resource.error(e, null));
            }
        };
        DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
        if (increase) {
            saveIncreaseCountEvent(ref.getKey(), model);
        } else {
            saveDecreaseCountEvent(ref.getKey(), model);
        }
        updateCountImpl(increase, ref, model, callback);
        return res;
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

    protected String EVENT_SAVE_ITEM = "eventSaveItem";
    protected String EVENT_EDIT_ITEM = "eventEditItem";
    protected String EVENT_DELETE_ITEM = "eventDeleteItem";
    protected String EVENT_DECREASE_COUNT = "eventDecreaseCount";
    protected String EVENT_INCREASE_COUNT = "eventIncreaseCount";

    protected String getUserUID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return null;
        return currentUser.getUid();
    }

    protected abstract void saveSaveItemEvent(String itemKey, Model model);
    protected abstract void saveEditItemEvent(String itemKey, Model modelOld, Model modelNew);
    protected abstract void saveDeleteItemEvent(String itemKey, Model model);
    protected abstract void saveDecreaseCountEvent(String itemKey, Model model);
    protected abstract void saveIncreaseCountEvent(String itemKey, Model model);

    protected void saveEvent(String eventName, Map<String, Object> payload) {
        FirebaseEventEntity event = new FirebaseEventEntity(eventName, getUserUID(), payload);
        DatabaseReference ref = dbReference.child(getEventsNode()).push();
        ref.setValue(event);
    }

    private MutableLiveData<Resource<Boolean>> connectedLiveData;
    @Override
    public LiveData<Resource<Boolean>> isConnected() {
        if (connectedLiveData == null) {
            connectedLiveData = new MutableLiveData<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(".info/connected");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean connected = dataSnapshot.getValue(Boolean.class);
                    connectedLiveData.setValue(Resource.success(connected));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    connectedLiveData.setValue(Resource.error(databaseError.toException(), null));
                }
            });
        }
        return connectedLiveData;
    }
}

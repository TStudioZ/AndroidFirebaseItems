package com.tstudioz.androidfirebaseitems.domain.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tstudioz.androidfirebaseitems.domain.mapper.FirebaseItemMapper;
import com.tstudioz.androidfirebaseitems.domain.model.FirebaseEventEntity;
import com.tstudioz.androidfirebaseitems.domain.response.UpdateCountResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Cancellable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public abstract class FirebaseDatabaseItemRepository<Model, Entity> implements IFirebaseDatabaseItemRepository<Model> {

    protected final DatabaseReference dbReference;
    protected final FirebaseItemMapper<Entity, Model> mapper;
    private final Query itemsQuery;

    private Subject<Model> saveModelEvent = BehaviorSubject.create();
    private Subject<Model> updateModelEvent = BehaviorSubject.create();
    private Subject<Model> deleteModelEvent = BehaviorSubject.create();

    private void saveModelEventOnNext(Model model) {
        saveModelEvent.onNext(model);
    }

    private void saveModelEventOnError(Throwable t) {
        saveModelEvent.onError(t);
    }

    private void updateModelEventOnNext(Model model) {
        updateModelEvent.onNext(model);
    }

    private void updateModelEventOnError(Throwable t) {
        updateModelEvent.onError(t);
    }

    private void deleteModelEventOnNext(Model model) {
        deleteModelEvent.onNext(model);
    }

    private void deleteModelEventOnError(Throwable t) {
        deleteModelEvent.onError(t);
    }

    @Override
    public Observable<Model> getSaveModelEvent() {
        return saveModelEvent;
    }

    @Override
    public Observable<Model> getUpdateModelEvent() {
        return updateModelEvent;
    }

    @Override
    public Observable<Model> getDeleteModelEvent() {
        return deleteModelEvent;
    }

    protected abstract String getModelsNode();
    protected abstract String getEventsNode();
    protected abstract String getModelKey(Model model);
    protected abstract void setModelKey(Model model, String key);
    protected abstract Model cloneModel(Model model);
    protected abstract void updateCountImpl(boolean increase, DatabaseReference ref, Model model, DataItemCallback<Model> callback);
    protected abstract Query createItemsQuery(DatabaseReference ref);

    public FirebaseDatabaseItemRepository(FirebaseItemMapper<Entity, Model> mapper) {
        this.dbReference = FirebaseDatabase.getInstance().getReference();
        this.mapper = mapper;
        this.itemsQuery = createItemsQuery(dbReference.child(getModelsNode()));
    }

    @Override
    public Observable<List<Model>> loadItems() {
        return Observable.create(new ObservableOnSubscribe<List<Model>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model>> emitter) throws Exception {
                ListBaseValueEventListener<Model, Entity> listener
                        = new ListBaseValueEventListener<>(mapper, new FirebaseDatabaseRepositoryCallback<Model>() {
                    @Override
                    public void onSuccess(List<Model> result) {
                        if (!emitter.isDisposed())
                            emitter.onNext(result);
                    }

                    @Override
                    public void onError(Exception e) {
                        if (!emitter.isDisposed())
                            emitter.onError(e);
                    }
                });
                itemsQuery.addValueEventListener(listener);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        itemsQuery.removeEventListener(listener);
                        Log.d("Repository", "loadItems cancelled");
                    }
                });
            }
        });
    }

    @Override
    public Single<Model> loadModel(String key) {
        return Single.create(new SingleOnSubscribe<Model>() {
            @Override
            public void subscribe(SingleEmitter<Model> emitter) throws Exception {
                final DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
                final ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Model model = mapper.map(key, dataSnapshot);
                        if (!emitter.isDisposed())
                            emitter.onSuccess(model);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (!emitter.isDisposed())
                            emitter.onError(databaseError.toException());
                    }
                };
                ref.addListenerForSingleValueEvent(listener);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        Log.d("Repository", "loadModel cancelled");
                    }
                });
            }
        });
    }

    @Override
    public Single<Model> save(Model modelOld, Model modelNew) {
        return Single.create(new SingleOnSubscribe<Model>() {
            @Override
            public void subscribe(SingleEmitter<Model> emitter) throws Exception {
                final Entity entity = mapper.mapToSource(modelNew);
                final String key = getModelKey(modelNew);
                DatabaseReference ref;
                if (key == null) {
                    ref = dbReference.child(getModelsNode()).push();
                    ref.setValue(entity).addOnSuccessListener(aVoid -> {
                        saveSaveItemEvent(ref.getKey(), modelNew);
                        setModelKey(modelNew, ref.getKey());

                        Log.d("Repository", "save onNext()");
                        saveModelEventOnNext(modelNew);
                        if (!emitter.isDisposed())
                            emitter.onSuccess(modelNew);
                    }).addOnFailureListener(e -> {
                        //new LiveDataEventWithTaggedObservers<>(Resource.error(e, modelNew))
                        saveModelEventOnError(e);
                    });
                } else {
                    ref = dbReference.child(getModelsNode()).child(key);
                    ref.setValue(entity).addOnSuccessListener(aVoid -> {
                        saveEditItemEvent(ref.getKey(), modelOld, modelNew);
                        setModelKey(modelNew, ref.getKey());

                        Log.d("Repository", "update onNext()");
                        updateModelEventOnNext(modelOld);
                        if (!emitter.isDisposed())
                            emitter.onSuccess(modelOld);
                    }).addOnFailureListener(FirebaseDatabaseItemRepository.this::updateModelEventOnError);
                }
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        Log.d("Repository", "save cancelled");
                    }
                });
            }
        });
    }

    @Override
    public Single<Model> delete(Model model) {
        return Single.create(new SingleOnSubscribe<Model>() {
            @Override
            public void subscribe(SingleEmitter<Model> emitter) throws Exception {
                String key = getModelKey(model);
                if (key == null) {
                    if (!emitter.isDisposed())
                        emitter.onError(new Exception("Key cannot be null"));
                    return;
                }

                DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
                ref.removeValue().addOnSuccessListener(aVoid -> {
                    saveDeleteItemEvent(ref.getKey(), model);
                    setModelKey(model, ref.getKey());

                    deleteModelEventOnNext(model);
                    if (!emitter.isDisposed())
                        emitter.onSuccess(model);
                }).addOnFailureListener(FirebaseDatabaseItemRepository.this::deleteModelEventOnError);
            }
        });
    }

    @Override
    public Single<UpdateCountResponse<Model>> decreaseCount(Model model) {
        return updateCount(model, false);
    }

    @Override
    public Single<UpdateCountResponse<Model>> increaseCount(Model model) {
        return updateCount(model, true);
    }

    private Single<UpdateCountResponse<Model>> updateCount(Model model, boolean increase) {
        return Single.create(new SingleOnSubscribe<UpdateCountResponse<Model>>() {
            @Override
            public void subscribe(SingleEmitter<UpdateCountResponse<Model>> emitter) throws Exception {
                Model cloned = cloneModel(model);
                String key = getModelKey(model);

                DataItemCallback<Model> callback = new DataItemCallback<Model>() {
                    @Override
                    public void onSuccess(Model result) {
                        if (!emitter.isDisposed())
                            emitter.onSuccess(new UpdateCountResponse<>(model, result == null));
                    }

                    @Override
                    public void onError(Exception e) {
                        if (!emitter.isDisposed())
                            emitter.onError(e);
                    }
                };
                DatabaseReference ref = dbReference.child(getModelsNode()).child(key);
                if (increase) {
                    saveIncreaseCountEvent(ref.getKey(), model);
                } else {
                    saveDecreaseCountEvent(ref.getKey(), model);
                }
                updateCountImpl(increase, ref, model, callback);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        Log.d("Repository", "updateCount cancelled");
                    }
                });
            }
        });
    }

    protected static final String EVENT_SAVE_ITEM = "eventSaveItem";
    protected static final String EVENT_EDIT_ITEM = "eventEditItem";
    protected static final String EVENT_DELETE_ITEM = "eventDeleteItem";
    protected static final String EVENT_DECREASE_COUNT = "eventDecreaseCount";
    protected static final String EVENT_INCREASE_COUNT = "eventIncreaseCount";

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

    @Override
    public Observable<Boolean> isConnected() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(".info/connected");
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean connected = dataSnapshot.getValue(Boolean.class);
                        if (!emitter.isDisposed())
                            emitter.onNext(connected);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (!emitter.isDisposed())
                            emitter.onError(databaseError.toException());
                    }
                };
                ref.addValueEventListener(listener);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        ref.removeEventListener(listener);
                        Log.d("Repository", "isConnected cancelled");
                    }
                });
            }
        });
    }
}

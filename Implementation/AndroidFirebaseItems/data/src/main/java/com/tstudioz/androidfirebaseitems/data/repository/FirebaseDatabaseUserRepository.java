package com.tstudioz.androidfirebaseitems.data.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tstudioz.androidfirebaseitems.data.mapper.FirebaseItemMapper;
import com.tstudioz.androidfirebaseitems.data.mapper.FirebaseUserMapper;
import com.tstudioz.androidfirebaseitems.data.model.FirebaseUserEntity;
import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Cancellable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class FirebaseDatabaseUserRepository implements IFirebaseDatabaseUserRepository {

    public static final String ROLE_BASIC = "basic";
    public static final String ROLE_EDITOR = "editor";
    private final DatabaseReference dbReference;
    private final FirebaseItemMapper<FirebaseUserEntity, FirebaseUser> mapper;

    private String getUsersNode() {
        return "users";
    }

    private Subject<FirebaseUser> saveUserEvent = BehaviorSubject.create();

    @Override
    public Observable<FirebaseUser> getSaveUserEvent() {
        return saveUserEvent;
    }

    public FirebaseDatabaseUserRepository() {
        this.dbReference = FirebaseDatabase.getInstance().getReference();
        this.mapper = new FirebaseUserMapper();
    }

    private void onUserLoadSuccess(DataSnapshot dataSnapshot, String uid, SingleEmitter<FirebaseUser> emitter) {
        FirebaseUser user = mapper.map(uid, dataSnapshot);
        if (!emitter.isDisposed())
            emitter.onSuccess(user);
    }

    private FirebaseUser createUser(String uid) {
        return new FirebaseUser(uid, ROLE_BASIC);
    }

    @Override
    public Single<FirebaseUser> registerLoadUser(String uid) {
        return Single.create(new SingleOnSubscribe<FirebaseUser>() {
            @Override
            public void subscribe(SingleEmitter<FirebaseUser> emitter) throws Exception {
                DatabaseReference ref = dbReference.child(getUsersNode()).child(uid);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            onUserLoadSuccess(dataSnapshot, uid, emitter);
                        } else {
                            FirebaseUser user = createUser(uid);
                            FirebaseUserEntity userEntity = mapper.mapToSource(user);
                            ref.setValue(userEntity).addOnSuccessListener(aVoid -> {
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        onUserLoadSuccess(dataSnapshot, uid, emitter);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        if (!emitter.isDisposed())
                                            emitter.onError(databaseError.toException());
                                    }
                                });
                            }).addOnFailureListener(e -> {
                                if (!emitter.isDisposed())
                                    emitter.onError(e);
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (!emitter.isDisposed())
                            emitter.onError(databaseError.toException());
                    }
                });
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        Log.d("Repository", "registerLoadUser cancelled");
                    }
                });
            }
        });
    }

    @Override
    public Single<FirebaseUser> save(FirebaseUser user) {
        return Single.create(new SingleOnSubscribe<FirebaseUser>() {
            @Override
            public void subscribe(SingleEmitter<FirebaseUser> emitter) throws Exception {
                FirebaseUserEntity entity = mapper.mapToSource(user);
                DatabaseReference ref;
                if (user.getUid() == null) {
                    ref = dbReference.child(getUsersNode()).push();
                } else {
                    ref = dbReference.child(getUsersNode()).child(user.getUid());
                }
                listenForResult(ref, user, ref.setValue(entity), emitter);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        Log.d("Repository", "saveUser cancelled");
                    }
                });
            }
        });
    }

    private <T> void listenForResult(DatabaseReference ref,
                                     FirebaseUser user,
                                     Task<T> task,
                                     SingleEmitter<FirebaseUser> emitter) {
        task.addOnSuccessListener(aVoid -> {
            user.setUid(ref.getKey());
            saveUserEvent.onNext(user);
            if (!emitter.isDisposed())
                emitter.onSuccess(user);
        }).addOnFailureListener(e -> {
            saveUserEvent.onError(e);
            if (!emitter.isDisposed())
                emitter.onError(e);
        });
    }
}

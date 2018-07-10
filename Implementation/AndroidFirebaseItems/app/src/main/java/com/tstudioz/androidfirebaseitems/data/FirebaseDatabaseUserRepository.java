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

public class FirebaseDatabaseUserRepository implements IFirebaseDatabaseUserRepository {

    private final DatabaseReference dbReference;
    private final FirebaseMapper<FirebaseUserEntity, FirebaseUser> mapper;

    private String getUsersNode() {
        return "users";
    }

    private MutableLiveData<LiveDataEventWithTaggedObservers<Resource<FirebaseUser>>> saveUserEvent = new MutableLiveData<>();

    @Override
    public LiveData<LiveDataEventWithTaggedObservers<Resource<FirebaseUser>>> getSaveUserEvent() {
        return saveUserEvent;
    }

    public FirebaseDatabaseUserRepository() {
        this.dbReference = FirebaseDatabase.getInstance().getReference();
        this.mapper = new FirebaseUserMapper();
    }

    private void onUserLoadSuccess(DataSnapshot dataSnapshot, String uid, MutableLiveData<Resource<FirebaseUser>> res) {
        FirebaseUser user = mapper.map(uid, dataSnapshot);
        res.setValue(Resource.success(user));
    }

    private FirebaseUser createUser(String uid) {
        return new FirebaseUser(uid, "basic");
    }

    @Override
    public LiveData<Resource<FirebaseUser>> registerLoadUser(String uid) {
        MutableLiveData<Resource<FirebaseUser>> res = new MutableLiveData<>();
        res.setValue(Resource.working(null));

        DatabaseReference ref = dbReference.child(getUsersNode()).child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    onUserLoadSuccess(dataSnapshot, uid, res);
                } else {
                    FirebaseUser user = createUser(uid);
                    FirebaseUserEntity userEntity = mapper.mapToSource(user);
                    ref.setValue(userEntity).addOnSuccessListener(aVoid -> {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                onUserLoadSuccess(dataSnapshot, uid, res);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                res.setValue(Resource.error(databaseError.toException(), null));
                            }
                        });
                    }).addOnFailureListener(e -> res.setValue(Resource.error(e, null)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                res.setValue(Resource.error(databaseError.toException(), null));
            }
        });
        return res;
    }

    @Override
    public void save(FirebaseUser user) {
        FirebaseUserEntity entity = mapper.mapToSource(user);
        DatabaseReference ref;
        if (user.getUid() == null) {
            ref = dbReference.child(getUsersNode()).push();
        } else {
            ref = dbReference.child(getUsersNode()).child(user.getUid());
        }
        listenForResult(ref, user, ref.setValue(entity), saveUserEvent);
    }

    private <T> void listenForResult(DatabaseReference ref,
                                     FirebaseUser user,
                                     Task<T> task,
                                     MutableLiveData<LiveDataEventWithTaggedObservers<Resource<FirebaseUser>>> event) {
        task.addOnSuccessListener(aVoid -> {
            user.setUid(ref.getKey());
            event.setValue(new LiveDataEventWithTaggedObservers<>(Resource.success(user)));
        }).addOnFailureListener(e -> {
            event.setValue(new LiveDataEventWithTaggedObservers<>(Resource.error(e, user)));
        });
    }
}

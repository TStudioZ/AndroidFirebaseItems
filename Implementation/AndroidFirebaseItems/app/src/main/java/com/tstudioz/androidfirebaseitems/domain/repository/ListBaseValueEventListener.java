package com.tstudioz.androidfirebaseitems.domain.repository;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tstudioz.androidfirebaseitems.domain.mapper.FirebaseItemMapper;

import java.util.List;

public class ListBaseValueEventListener<Model, Entity> implements ValueEventListener {

    private final FirebaseItemMapper<Entity, Model> mapper;
    private final FirebaseDatabaseItemRepository.FirebaseDatabaseRepositoryCallback<Model> callback;

    public ListBaseValueEventListener(FirebaseItemMapper<Entity, Model> mapper,
                                      FirebaseDatabaseItemRepository.FirebaseDatabaseRepositoryCallback<Model> callback) {
        this.mapper = mapper;
        this.callback = callback;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Model> data = mapper.mapList(dataSnapshot);
        callback.onSuccess(data);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        callback.onError(databaseError.toException());
    }
}

package com.tstudioz.androidfirebaseitems.data.repository;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tstudioz.androidfirebaseitems.data.mapper.FirebaseItemMapper;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import java.util.List;

public class ListBaseValueEventListener<Model, Entity> implements ValueEventListener {

    private final FirebaseItemMapper<Entity, Model> mapper;
    private final IFirebaseDatabaseItemRepository.FirebaseDatabaseRepositoryCallback<Model> callback;

    public ListBaseValueEventListener(FirebaseItemMapper<Entity, Model> mapper,
                                      IFirebaseDatabaseItemRepository.FirebaseDatabaseRepositoryCallback<Model> callback) {
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

package com.tstudioz.androidfirebaseitems.data.mapper;

import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.data.model.FirebaseUserEntity;

public class FirebaseUserMapper extends FirebaseItemMapper<FirebaseUserEntity, FirebaseUser> {

    @Override
    public FirebaseUserEntity mapToSource(FirebaseUser firebaseUser) {
        return new FirebaseUserEntity(firebaseUser.getRole());
    }

    @Override
    public FirebaseUser mapToDestination(String key, FirebaseUserEntity firebaseUserEntity) {
        return new FirebaseUser(key, firebaseUserEntity.getRole());
    }
}

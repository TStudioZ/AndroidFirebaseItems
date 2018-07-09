package com.tstudioz.androidfirebaseitems.data;

public class FirebaseUserMapper extends FirebaseMapper<FirebaseUserEntity, FirebaseUser> {

    @Override
    public FirebaseUserEntity mapToSource(FirebaseUser firebaseUser) {
        return new FirebaseUserEntity(firebaseUser.getRole());
    }

    @Override
    public FirebaseUser mapToDestination(String key, FirebaseUserEntity firebaseUserEntity) {
        return new FirebaseUser(key, firebaseUserEntity.getRole());
    }
}

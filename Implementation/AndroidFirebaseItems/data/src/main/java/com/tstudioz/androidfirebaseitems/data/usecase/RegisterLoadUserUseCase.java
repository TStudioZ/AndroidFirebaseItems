package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class RegisterLoadUserUseCase extends DatabaseUserUseCase {

    @Inject
    public RegisterLoadUserUseCase(IFirebaseDatabaseUserRepository repo) {
        super(repo);
    }

    public Single<FirebaseUser> execute(String uid) {
        return getRepo().registerLoadUser(uid);
    }
}

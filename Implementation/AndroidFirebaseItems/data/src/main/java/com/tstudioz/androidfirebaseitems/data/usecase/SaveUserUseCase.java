package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class SaveUserUseCase extends DatabaseUserUseCase {

    @Inject
    public SaveUserUseCase(IFirebaseDatabaseUserRepository repo) {
        super(repo);
    }

    public Single<FirebaseUser> execute(FirebaseUser user) {
        return getRepo().save(user);
    }
}

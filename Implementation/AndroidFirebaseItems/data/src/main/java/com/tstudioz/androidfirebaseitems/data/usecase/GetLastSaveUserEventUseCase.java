package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.FirebaseUser;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;

import io.reactivex.Observable;

public class GetLastSaveUserEventUseCase extends DatabaseUserUseCase {

    public GetLastSaveUserEventUseCase(IFirebaseDatabaseUserRepository repo) {
        super(repo);
    }

    public Observable<FirebaseUser> execute() {
        return getRepo().getSaveUserEvent();
    }
}

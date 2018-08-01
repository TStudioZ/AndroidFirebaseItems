package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;

public abstract class DatabaseUserUseCase {

    private final IFirebaseDatabaseUserRepository repo;

    protected IFirebaseDatabaseUserRepository getRepo() {
        return repo;
    }

    public DatabaseUserUseCase(IFirebaseDatabaseUserRepository repo) {
        this.repo = repo;
    }
}

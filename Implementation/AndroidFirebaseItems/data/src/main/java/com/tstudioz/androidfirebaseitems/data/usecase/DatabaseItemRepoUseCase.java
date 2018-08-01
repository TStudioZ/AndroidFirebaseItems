package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

public abstract class DatabaseItemRepoUseCase<T> {

    private final IFirebaseDatabaseItemRepository<T> repo;

    protected IFirebaseDatabaseItemRepository<T> getRepo() {
        return repo;
    }

    public DatabaseItemRepoUseCase(IFirebaseDatabaseItemRepository<T> repo) {
        this.repo = repo;
    }
}

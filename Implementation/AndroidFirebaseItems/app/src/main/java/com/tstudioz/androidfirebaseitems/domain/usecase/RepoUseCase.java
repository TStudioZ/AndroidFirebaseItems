package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

public abstract class RepoUseCase<T extends DataItem> {

    private final IFirebaseDatabaseItemRepository<T> repo;

    protected IFirebaseDatabaseItemRepository<T> getRepo() {
        return repo;
    }

    public RepoUseCase(IFirebaseDatabaseItemRepository<T> repo) {
        this.repo = repo;
    }
}

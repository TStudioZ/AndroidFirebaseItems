package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoadItemUseCase {

    private final IFirebaseDatabaseItemRepository<DataItem> repo;

    @Inject
    public LoadItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        this.repo = repo;
    }

    public Single<DataItem> execute(String key) {
        return repo.loadModel(key);
    }
}

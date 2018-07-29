package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LoadItemsUseCase {

    private final IFirebaseDatabaseItemRepository<DataItem> repo;

    @Inject
    public LoadItemsUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        this.repo = repo;
    }

    public Observable<List<DataItem>> execute() {
        return repo.loadItems();
    }
}

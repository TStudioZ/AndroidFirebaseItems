package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LoadItemsUseCase extends RepoUseCase<DataItem> {

    @Inject
    public LoadItemsUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Observable<List<DataItem>> execute() {
        return getRepo().loadItems();
    }
}

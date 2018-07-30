package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoadItemUseCase extends RepoUseCase<DataItem> {

    @Inject
    public LoadItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<DataItem> execute(String key) {
        return getRepo().loadModel(key);
    }
}

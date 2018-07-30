package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class SaveItemUseCase extends RepoUseCase<DataItem> {

    @Inject
    public SaveItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<DataItem> execute(DataItem itemOld, DataItem itemNew) {
        return getRepo().save(itemOld, itemNew);
    }
}

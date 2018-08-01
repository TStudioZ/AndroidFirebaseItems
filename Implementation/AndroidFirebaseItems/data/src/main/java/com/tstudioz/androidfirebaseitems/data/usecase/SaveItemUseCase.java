package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class SaveItemUseCase extends DatabaseItemRepoUseCase<DataItem> {

    @Inject
    public SaveItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<DataItem> execute(DataItem itemOld, DataItem itemNew) {
        return getRepo().save(itemOld, itemNew);
    }
}

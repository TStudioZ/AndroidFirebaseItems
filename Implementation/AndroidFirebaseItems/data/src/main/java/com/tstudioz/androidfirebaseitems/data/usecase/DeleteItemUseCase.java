package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class DeleteItemUseCase extends DatabaseItemRepoUseCase<DataItem> {

    @Inject
    public DeleteItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<DataItem> execute(DataItem item) {
        return getRepo().delete(item);
    }
}

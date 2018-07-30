package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class DeleteItemUseCase extends RepoUseCase<DataItem> {

    @Inject
    public DeleteItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<DataItem> execute(DataItem item) {
        return getRepo().delete(item);
    }
}

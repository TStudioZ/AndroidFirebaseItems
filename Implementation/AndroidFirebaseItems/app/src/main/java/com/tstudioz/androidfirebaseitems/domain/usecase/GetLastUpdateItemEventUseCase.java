package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetLastUpdateItemEventUseCase extends RepoUseCase<DataItem> {

    @Inject
    public GetLastUpdateItemEventUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Observable<DataItem> execute() {
        return getRepo().getUpdateModelEvent();
    }
}

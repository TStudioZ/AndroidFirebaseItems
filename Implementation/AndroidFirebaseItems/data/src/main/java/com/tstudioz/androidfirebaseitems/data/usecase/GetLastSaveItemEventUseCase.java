package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetLastSaveItemEventUseCase extends DatabaseItemRepoUseCase<DataItem> {

    @Inject
    public GetLastSaveItemEventUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Observable<DataItem> execute() {
        return getRepo().getSaveModelEvent();
    }
}

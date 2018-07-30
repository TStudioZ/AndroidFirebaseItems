package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class IsConnectedUseCase extends RepoUseCase<DataItem> {

    @Inject
    public IsConnectedUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Observable<Boolean> execute() {
        return getRepo().isConnected();
    }
}

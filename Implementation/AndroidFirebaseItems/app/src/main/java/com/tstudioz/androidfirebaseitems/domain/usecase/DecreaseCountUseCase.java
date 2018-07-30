package com.tstudioz.androidfirebaseitems.domain.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.response.UpdateCountResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class DecreaseCountUseCase extends RepoUseCase<DataItem> {

    @Inject
    public DecreaseCountUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<UpdateCountResponse<DataItem>> execute(DataItem item) {
        return getRepo().decreaseCount(item);
    }
}

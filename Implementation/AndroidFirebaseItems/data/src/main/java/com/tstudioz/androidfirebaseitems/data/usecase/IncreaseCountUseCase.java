package com.tstudioz.androidfirebaseitems.data.usecase;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.response.UpdateCountResponse;

import javax.inject.Inject;

import io.reactivex.Single;

public class IncreaseCountUseCase extends DatabaseItemRepoUseCase<DataItem> {

    @Inject
    public IncreaseCountUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        super(repo);
    }

    public Single<UpdateCountResponse<DataItem>> execute(DataItem item) {
        return getRepo().increaseCount(item);
    }
}

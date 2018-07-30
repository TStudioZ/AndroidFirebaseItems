package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.usecase.DecreaseCountUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.DeleteItemUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.GetLastDeleteItemEventUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.GetLastSaveItemEventUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.GetLastUpdateItemEventUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.IncreaseCountUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.IsConnectedUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemsUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.SaveItemUseCase;

import dagger.Module;
import dagger.Provides;

@Module
abstract class UseCaseModule {

    @Provides
    static LoadItemsUseCase bindLoadItemsUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new LoadItemsUseCase(repo);
    }

    @Provides
    static LoadItemUseCase bindLoadItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new LoadItemUseCase(repo);
    }

    @Provides
    static SaveItemUseCase bindSaveItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new SaveItemUseCase(repo);
    }

    @Provides
    static DeleteItemUseCase bindDeleteItemUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new DeleteItemUseCase(repo);
    }

    @Provides
    static GetLastSaveItemEventUseCase bindGetLastSaveItemEventUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new GetLastSaveItemEventUseCase(repo);
    }

    @Provides
    static GetLastUpdateItemEventUseCase bindGetLastUpdateItemEventUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new GetLastUpdateItemEventUseCase(repo);
    }

    @Provides
    static GetLastDeleteItemEventUseCase bindGetLastDeleteItemEventUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new GetLastDeleteItemEventUseCase(repo);
    }

    @Provides
    static IncreaseCountUseCase bindIncreaseCountUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new IncreaseCountUseCase(repo);
    }

    @Provides
    static DecreaseCountUseCase bindDecreaseCountUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new DecreaseCountUseCase(repo);
    }

    @Provides
    static IsConnectedUseCase bindIsConnectedUseCase(IFirebaseDatabaseItemRepository<DataItem> repo) {
        return new IsConnectedUseCase(repo);
    }
}

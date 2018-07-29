package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemUseCase;
import com.tstudioz.androidfirebaseitems.domain.usecase.LoadItemsUseCase;

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
}

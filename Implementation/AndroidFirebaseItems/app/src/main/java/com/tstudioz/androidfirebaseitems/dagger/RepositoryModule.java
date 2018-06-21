package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.DataItemRepository;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
abstract class RepositoryModule {

    @Provides
    @Singleton
    static IFirebaseDatabaseRepository<DataItem> bindDataRepository() {
        return new DataItemRepository();
    }
}

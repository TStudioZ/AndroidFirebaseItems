package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.DataItemRepository;
import com.tstudioz.androidfirebaseitems.data.FirebaseDatabaseUserRepository;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseUserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
abstract class RepositoryModule {

    @Provides
    @Singleton
    static IFirebaseDatabaseItemRepository<DataItem> bindDataRepository() {
        return new DataItemRepository();
    }

    @Provides
    @Singleton
    static IFirebaseDatabaseUserRepository bindUserRepository() {
        return new FirebaseDatabaseUserRepository();
    }
}

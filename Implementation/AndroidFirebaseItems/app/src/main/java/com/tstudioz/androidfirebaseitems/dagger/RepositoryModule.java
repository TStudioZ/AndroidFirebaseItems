package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.domain.model.DataItem;
import com.tstudioz.androidfirebaseitems.domain.repository.DataItemRepository;
import com.tstudioz.androidfirebaseitems.domain.repository.FirebaseDatabaseUserRepository;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.domain.repository.IFirebaseDatabaseUserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

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

package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseRepository;

import dagger.Binds;
import dagger.Module;

@Module
abstract class RepositoryModule {

    @Binds
    abstract IFirebaseDatabaseRepository bindDataRepository(IFirebaseDatabaseRepository dataRepository);
}

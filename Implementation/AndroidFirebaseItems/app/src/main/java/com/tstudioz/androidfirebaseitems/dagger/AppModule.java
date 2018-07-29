package com.tstudioz.androidfirebaseitems.dagger;

import dagger.Module;

@Module(includes = {
        ViewModelModule.class,
        UseCaseModule.class,
        RepositoryModule.class})
class AppModule {
}

package com.tstudioz.androidfirebaseitems.dagger;

import dagger.Module;

@Module(includes = {
        ViewModelModule.class,
        RepositoryModule.class})
class AppModule {
}

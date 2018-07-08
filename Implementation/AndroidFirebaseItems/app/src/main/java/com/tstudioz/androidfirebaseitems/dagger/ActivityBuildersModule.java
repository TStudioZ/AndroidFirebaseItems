package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.ui.AddEditItemActivity;
import com.tstudioz.androidfirebaseitems.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract AddEditItemActivity contributeAddEditItemActivity();
}

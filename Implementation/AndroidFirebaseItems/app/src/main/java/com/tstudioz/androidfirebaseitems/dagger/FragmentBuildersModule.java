package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.androidfirebaseitems.ui.ItemsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ItemsFragment contributeItemsFragment();
}

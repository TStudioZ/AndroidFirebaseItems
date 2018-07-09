package com.tstudioz.androidfirebaseitems.dagger;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.tstudioz.androidfirebaseitems.viewmodel.ItemViewModel;
import com.tstudioz.androidfirebaseitems.viewmodel.ItemsViewModel;
import com.tstudioz.essentialuilibrary.dagger.ViewModelKey;
import com.tstudioz.essentialuilibrary.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ItemViewModel.class)
    abstract ViewModel bindItemViewModel(ItemViewModel itemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemsViewModel.class)
    abstract ViewModel bindItemsViewModel(ItemsViewModel itemsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}

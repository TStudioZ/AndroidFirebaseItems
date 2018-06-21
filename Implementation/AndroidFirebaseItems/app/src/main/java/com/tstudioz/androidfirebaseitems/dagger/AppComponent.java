package com.tstudioz.androidfirebaseitems.dagger;

import com.tstudioz.FirebaseItemsApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuildersModule.class
})
public interface AppComponent extends AndroidInjector<FirebaseItemsApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<FirebaseItemsApp> {
        @Override
        public void seedInstance(FirebaseItemsApp instance) {

        }
    }
}

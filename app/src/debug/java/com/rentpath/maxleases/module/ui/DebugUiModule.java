package com.rentpath.maxleases.module.ui;

import com.gf.movie.reminder.ui.AppContainer;
import com.rentpath.maxleases.ui.DebugAppContainer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = DebugAppContainer.class,
        complete = false,
        library = true,
        overrides = true
)
public class DebugUiModule {
    @Provides
    @Singleton
    AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
        //return AppContainer.DEFAULT;
        return debugAppContainer;
    }
}
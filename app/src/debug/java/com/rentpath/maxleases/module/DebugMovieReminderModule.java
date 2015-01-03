package com.rentpath.maxleases.module;

import com.gf.movie.reminder.module.MovieReminderModule;
import com.rentpath.maxleases.module.ui.DebugUiModule;

import dagger.Module;

@Module(
        addsTo = MovieReminderModule.class,
        includes = {
                DebugUiModule.class,
                DebugDataModule.class
        },
        overrides = true,
        complete = false
)
public final class DebugMovieReminderModule {
}
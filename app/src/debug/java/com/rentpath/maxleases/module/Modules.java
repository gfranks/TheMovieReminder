package com.rentpath.maxleases.module;

import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.module.MovieReminderModule;

public final class Modules {

    private Modules() {
        // No instances.
    }

    public static Object[] list(MovieReminderApplication app) {
        return new Object[]{
                new MovieReminderModule(app),
                new DebugMovieReminderModule(),
        };
    }
}
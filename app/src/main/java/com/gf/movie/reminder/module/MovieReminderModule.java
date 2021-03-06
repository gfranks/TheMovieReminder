package com.gf.movie.reminder.module;

import android.app.Application;

import com.gf.movie.reminder.activity.GameTrailerActivity;
import com.gf.movie.reminder.activity.LogInActivity;
import com.gf.movie.reminder.activity.MainActivity;
import com.gf.movie.reminder.activity.MovieTrailerActivity;
import com.gf.movie.reminder.activity.TrailerSearchResultsActivity;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.activity.base.BaseTrailerActivity;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.module.ui.UiModule;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                UiModule.class,
                DataModule.class,
                UserModule.class,
        },
        injects = {
                BaseActivity.class,
                MainActivity.class,
                BaseTrailerActivity.class,
                MovieTrailerActivity.class,
                GameTrailerActivity.class,
                TrailerSearchResultsActivity.class,
                LogInActivity.class
        },
        library = true,
        complete = false
)
public class MovieReminderModule {

    private MovieReminderApplication mApplication;

    public MovieReminderModule(MovieReminderApplication application) {
        this.mApplication = application;
    }

    public static List<Object> list(MovieReminderApplication app) {
        return Arrays.<Object>asList(
                new MovieReminderModule(app)
        );
    }

    @Provides
    @Singleton
    public MovieReminderApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Application provideApplicationContext() {
        return mApplication;
    }
}

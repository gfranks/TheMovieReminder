package com.gf.movie.reminder.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.util.GameReminderManager;
import com.gf.movie.reminder.util.MovieReminderManager;
import com.gf.movie.reminder.util.NotificationManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module(
        includes = {
                ApiModule.class
        },
        injects = {
                NotificationManager.class
        },
        complete = false,
        library = true
)
public class DataModule {

    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final String MAXLEASES_CACHE = "maxleases-cache";

    static OkHttpClient createOkHttpClient(Context context, File cacheDir) {
        OkHttpClient client = new OkHttpClient();
        // Install an HTTP cache in the application cache directory.
        try {
            File httpCacheDir = new File(cacheDir, "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                Log.d(MovieReminderApplication.TAG, "Unable to install disk cache.", e);
            }
        }
        return client;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app, File cacheDir) {
        return createOkHttpClient(app, cacheDir);
    }

    @Provides
    public File provideCacheDir(Application app) {
        File cache = new File(app.getCacheDir(), MAXLEASES_CACHE);
        if (!cache.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cache.mkdirs();
        }
        return cache;
    }

    @Provides
    @Singleton
    Picasso providePicasso(Application app) {
        return Picasso.with(app);
    }

    @Provides
    @Singleton
    Realm provideRealm(Application app) {
        return Realm.getInstance(app);
    }

    @Provides
    @Singleton
    NotificationManager provideMovieNotificationManager(AccountManager accountManager, MovieReminderManager reminderManager) {
        return NotificationManager.getInstance(accountManager, reminderManager);
    }

    @Provides
    @Singleton
    MovieReminderManager provideMovieReminderManager(Application app) {
        return MovieReminderManager.getInstance(app);
    }

    @Provides
    @Singleton
    GameReminderManager provideGameReminderManager(Application app) {
        return GameReminderManager.getInstance(app);
    }
}

package com.gf.movie.reminder.module;

import android.app.Application;
import android.content.SharedPreferences;

import com.gf.movie.reminder.util.AccountManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
        },
        complete = false,
        library = true
)
public class UserModule {

    @Provides
    @Singleton
    AccountManager provideAccountManager(SharedPreferences sharedPreferences,
                                         Application appContext) {
        return new AccountManager(sharedPreferences, appContext);
    }
}

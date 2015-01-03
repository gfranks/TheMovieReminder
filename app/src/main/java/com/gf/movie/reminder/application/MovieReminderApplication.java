package com.gf.movie.reminder.application;

import android.app.Application;
import android.content.Context;

import com.rentpath.maxleases.module.Modules;

import dagger.ObjectGraph;

public class MovieReminderApplication extends Application {

    public static final String TAG = "TheMovieReminder";

    private ObjectGraph mObjectGraph;

    public static MovieReminderApplication get(Context context) {
        return (MovieReminderApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildObjectGraph();
    }

    public void buildObjectGraph() {
        mObjectGraph = ObjectGraph.create(Modules.list(this));
    }

    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    public ObjectGraph getApplicationObjectGraph() {
        return mObjectGraph;
    }
}

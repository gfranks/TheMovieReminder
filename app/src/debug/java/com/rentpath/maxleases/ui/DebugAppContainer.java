package com.rentpath.maxleases.ui;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.ui.AppContainer;
import com.gf.movie.reminder.ui.AppContainerContentInterface;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggle;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggleListener;
import com.gf.movie.reminder.view.MovieReminderDrawerLayout;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DebugAppContainer implements AppContainer {

    @Inject
    public DebugAppContainer() {
    }

    @Override
    public MovieReminderDrawerLayout setContentView(final ActionBarActivity activity, MovieReminderApplication app, int layoutResID, boolean overlayToolbar) {
        if (activity instanceof AppContainerContentInterface) {
            int layoutId = overlayToolbar ? R.layout.debug_activity_frame_toolbar_overlay : R.layout.debug_activity_frame;
            ((AppContainerContentInterface) activity).setActivityContent(layoutId);
        } else {
            throw new IllegalArgumentException("Activity must inherit AppContainerContentInterface or extend BaseActivity");
        }

        MovieReminderDrawerLayout drawer = addDevSettingsDrawerComponent(activity);
        drawer.setDrawerListener(new AppContainerActionBarDrawerToggle(activity, drawer));
        drawer.setDrawerShadow(R.drawable.img_shadow_left, GravityCompat.START);
        drawer.setDrawerShadow(R.drawable.img_shadow_right, GravityCompat.END);
        drawer.setDrawerLockMode(MovieReminderDrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);

        ViewGroup content = ((ViewGroup) activity.findViewById(R.id.activity_content));
        LayoutInflater.from(activity).inflate(layoutResID, content);

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.activity_toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        return drawer;
    }

    @Override
    public MovieReminderDrawerLayout addNavigationDrawerComponent(ActionBarActivity activity, MovieReminderApplication app, AppContainerActionBarDrawerToggleListener drawerToggleListener) {
        return AppContainer.DEFAULT.addNavigationDrawerComponent(activity, app, drawerToggleListener);
    }

    @Override
    public MovieReminderDrawerLayout addDevSettingsDrawerComponent(ActionBarActivity activity) {
        return AppContainer.DEFAULT.addDevSettingsDrawerComponent(activity);
    }
}

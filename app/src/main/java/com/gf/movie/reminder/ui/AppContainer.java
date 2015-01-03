package com.gf.movie.reminder.ui;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.fragment.DevSettingsFragment;
import com.gf.movie.reminder.fragment.NavigationFragment;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggle;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggleListener;
import com.gf.movie.reminder.view.MovieReminderDrawerLayout;

public interface AppContainer {

    /**
     * An {@link AppContainer} which returns the normal activity content view.
     */
    AppContainer DEFAULT = new AppContainer() {

        @Override
        public MovieReminderDrawerLayout setContentView(final ActionBarActivity activity, MovieReminderApplication app, int layoutResID, boolean overlayToolbar) {
            if (activity instanceof AppContainerContentInterface) {
                int layoutId = overlayToolbar ? R.layout.activity_frame_toolbar_overlay : R.layout.activity_frame;
                ((AppContainerContentInterface) activity).setActivityContent(layoutId);
            } else {
                throw new IllegalArgumentException("Activity must inherit AppContainerContentInterface or extend BaseActivity");
            }
            MovieReminderDrawerLayout drawer = (MovieReminderDrawerLayout) activity.findViewById(R.id.activity_drawer_layout);
            drawer.setDrawerListener(new AppContainerActionBarDrawerToggle(activity, drawer));
            drawer.setDrawerShadow(R.drawable.img_shadow_left, GravityCompat.START);
            drawer.setDrawerShadow(R.drawable.img_shadow_right, GravityCompat.END);
            drawer.setDrawerLockMode(MovieReminderDrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
            drawer.setDrawerLockMode(MovieReminderDrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

            ViewGroup content = ((ViewGroup) activity.findViewById(R.id.activity_content));
            LayoutInflater.from(activity).inflate(layoutResID, content);

            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.activity_toolbar);
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(true);

            if (activity.getSharedPreferences(MovieReminderApplication.TAG, Context.MODE_PRIVATE).getBoolean("DEV_SETTINGS_ENABLED", false)) {
                addDevSettingsDrawerComponent(activity);
            }

            return drawer;
        }

        @Override
        public MovieReminderDrawerLayout addNavigationDrawerComponent(ActionBarActivity activity, MovieReminderApplication app, AppContainerActionBarDrawerToggleListener drawerToggleListener) {
            final MovieReminderDrawerLayout drawer = (MovieReminderDrawerLayout) activity.findViewById(R.id.activity_drawer_layout);
            if (drawer.getDrawerListener() != null) {
                ((AppContainerActionBarDrawerToggle) drawer.getDrawerListener()).setListener(drawerToggleListener);
            } else {
                drawer.setDrawerListener(new AppContainerActionBarDrawerToggle(activity, drawer, drawerToggleListener));
            }
            drawer.setDrawerLockMode(MovieReminderDrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
            NavigationFragment fragment = (NavigationFragment) activity.getSupportFragmentManager().findFragmentByTag(NavigationFragment.TAG);
            if (fragment == null) {
                fragment = new NavigationFragment();
            }
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_nav_drawer, fragment, NavigationFragment.TAG);
            ft.addToBackStack(null);
            ft.commit();

            return drawer;
        }

        @Override
        public MovieReminderDrawerLayout addDevSettingsDrawerComponent(ActionBarActivity activity) {
            MovieReminderDrawerLayout drawer = (MovieReminderDrawerLayout) activity.findViewById(R.id.activity_drawer_layout);
            drawer.setDrawerLockMode(MovieReminderDrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);

            DevSettingsFragment fragment = (DevSettingsFragment) activity.getSupportFragmentManager().findFragmentByTag(DevSettingsFragment.TAG);
            if (fragment == null) {
                fragment = new DevSettingsFragment();
            }
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activity_dev_drawer, fragment, DevSettingsFragment.TAG);
            ft.commit();

            return drawer;
        }
    };

    MovieReminderDrawerLayout setContentView(ActionBarActivity activity, MovieReminderApplication app, int layoutResID, boolean overlayToolbar);

    MovieReminderDrawerLayout addNavigationDrawerComponent(ActionBarActivity activity, MovieReminderApplication app, AppContainerActionBarDrawerToggleListener drawerToggleListener);

    MovieReminderDrawerLayout addDevSettingsDrawerComponent(ActionBarActivity activity);
}

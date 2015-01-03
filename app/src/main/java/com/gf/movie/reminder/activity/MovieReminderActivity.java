package com.gf.movie.reminder.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.fragment.NavigationFragment;
import com.gf.movie.reminder.fragment.RemindersFragment;
import com.gf.movie.reminder.fragment.TrailersFragment;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.ui.NavigationListItem;
import com.gf.movie.reminder.util.MovieNotificationManager;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class MovieReminderActivity extends BaseActivity implements NavigationFragment.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String LAST_SELECTED_NAV_ITEM = "last_selected_nav_item";

    @Inject
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reminder);
        addNavigationDrawerComponent();

        mExpandableFab.setVisibility(View.VISIBLE);
        mExpandableFab.animateVisibility(0f, 1f, View.VISIBLE);
        mExpandableFab.setFadeOnFabClick(false);
        mExpandableFab.setOnFabClickListener(this);

        onItemSelected(NavigationListItem.values()[mPrefs.getInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.TRAILER.ordinal())]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentByTag(NavigationFragment.TAG);
        if (fragment != null) {
            fragment.setOnNavigationItemSelectedListener(this);
        }

        if (getIntent().hasExtra(MovieNotificationManager.EXTRA_MOVIE_REMINDER)) {
            MovieReminder reminder = getIntent().getParcelableExtra(MovieNotificationManager.EXTRA_MOVIE_REMINDER);
            getFeedbackBar().showInfo(String.format(getString(R.string.trailers_movie_notified), reminder.getMovie().getTitle()), false, FeedbackBar.LENGTH_LONG);
            getIntent().removeExtra(MovieNotificationManager.EXTRA_MOVIE_REMINDER);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_reminder_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(NavigationListItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item) {
            case TRAILER:
                mPrefs.edit().putInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.TRAILER.ordinal()).apply();
                mExpandableFab.slideInFab();
                TrailersFragment trailersFragment = (TrailersFragment) getSupportFragmentManager().findFragmentByTag(TrailersFragment.TAG);
                if (trailersFragment == null) {
                    trailersFragment = new TrailersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, trailersFragment, TrailersFragment.TAG).commit();
                }
                break;
            case REMINDERS:
                mPrefs.edit().putInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.REMINDERS.ordinal()).apply();
                mExpandableFab.slideOutFab();
                RemindersFragment remindersFragment = (RemindersFragment) getSupportFragmentManager().findFragmentByTag(RemindersFragment.TAG);
                if (remindersFragment == null) {
                    remindersFragment = new RemindersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, remindersFragment, RemindersFragment.TAG).commit();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mExpandableFab.isViewFabView(v)) {
            BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.activity_content);
            if (fragment != null) {
                fragment.onFabClick();
            }
        }
    }
}

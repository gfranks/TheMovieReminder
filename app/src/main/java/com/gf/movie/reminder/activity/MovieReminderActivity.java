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
import com.gf.movie.reminder.fragment.GameRemindersFragment;
import com.gf.movie.reminder.fragment.GameTrailersFragment;
import com.gf.movie.reminder.fragment.MovieRemindersFragment;
import com.gf.movie.reminder.fragment.MovieTrailersFragment;
import com.gf.movie.reminder.fragment.NavigationFragment;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.gf.movie.reminder.ui.NavigationListItem;
import com.gf.movie.reminder.util.NotificationManager;
import com.gf.movie.reminder.view.FeedbackBar;

import javax.inject.Inject;

public class MovieReminderActivity extends BaseActivity implements NavigationFragment.OnNavigationItemSelectedListener,
        View.OnClickListener {

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

        onItemSelected(NavigationListItem.values()[mPrefs.getInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.MOVIE_TRAILERS.ordinal())]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentByTag(NavigationFragment.TAG);
        if (fragment != null) {
            fragment.setOnNavigationItemSelectedListener(this);
        }

        if (getIntent().hasExtra(NotificationManager.EXTRA_MOVIE_REMINDER)) {
            MovieReminder reminder = getIntent().getParcelableExtra(NotificationManager.EXTRA_MOVIE_REMINDER);
            getFeedbackBar().showInfo(String.format(getString(R.string.trailers_movie_notified), reminder.getTrailer().getTitle()), false, FeedbackBar.LENGTH_LONG);
            getIntent().removeExtra(NotificationManager.EXTRA_MOVIE_REMINDER);
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
            case MOVIE_TRAILERS:
                mPrefs.edit().putInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.MOVIE_TRAILERS.ordinal()).apply();
                mExpandableFab.slideInFab();
                MovieTrailersFragment movieTrailersFragment = (MovieTrailersFragment) getSupportFragmentManager().findFragmentByTag(MovieTrailersFragment.TAG);
                if (movieTrailersFragment == null) {
                    movieTrailersFragment = new MovieTrailersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, movieTrailersFragment, MovieTrailersFragment.TAG).commit();
                }
                break;
            case MOVIE_REMINDERS:
                mPrefs.edit().putInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.MOVIE_REMINDERS.ordinal()).apply();
                mExpandableFab.slideOutFab();
                MovieRemindersFragment movieRemindersFragment = (MovieRemindersFragment) getSupportFragmentManager().findFragmentByTag(MovieRemindersFragment.TAG);
                if (movieRemindersFragment == null) {
                    movieRemindersFragment = new MovieRemindersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, movieRemindersFragment, MovieRemindersFragment.TAG).commit();
                }
                break;
            case GAME_TRAILERS:
                mPrefs.edit().putInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.GAME_TRAILERS.ordinal()).apply();
                mExpandableFab.slideInFab();
                GameTrailersFragment gameTrailersFragment = (GameTrailersFragment) getSupportFragmentManager().findFragmentByTag(GameTrailersFragment.TAG);
                if (gameTrailersFragment == null) {
                    gameTrailersFragment = new GameTrailersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, gameTrailersFragment, GameTrailersFragment.TAG).commit();
                }
                break;
            case GAME_REMINDERS:
                mPrefs.edit().putInt(LAST_SELECTED_NAV_ITEM, NavigationListItem.GAME_REMINDERS.ordinal()).apply();
                mExpandableFab.slideOutFab();
                GameRemindersFragment gameRemindersFragment = (GameRemindersFragment) getSupportFragmentManager().findFragmentByTag(GameRemindersFragment.TAG);
                if (gameRemindersFragment == null) {
                    gameRemindersFragment = new GameRemindersFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_content, gameRemindersFragment, GameRemindersFragment.TAG).commit();
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

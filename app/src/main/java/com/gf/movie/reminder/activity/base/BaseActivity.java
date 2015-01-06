package com.gf.movie.reminder.activity.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gf.movie.reminder.BuildConfig;
import com.gf.movie.reminder.R;
import com.gf.movie.reminder.application.MovieReminderApplication;
import com.gf.movie.reminder.fragment.NavigationFragment;
import com.gf.movie.reminder.module.ActivityModule;
import com.gf.movie.reminder.ui.AppContainer;
import com.gf.movie.reminder.ui.AppContainerContentInterface;
import com.gf.movie.reminder.ui.ExpandableFab;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.util.AppContainerActionBarDrawerToggleListener;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.MovieReminderDrawerLayout;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public abstract class BaseActivity extends ActionBarActivity implements AppContainerContentInterface, AppContainerActionBarDrawerToggleListener {

    protected Toolbar mToolbar;
    protected MovieReminderDrawerLayout mDrawerLayout;
    protected FeedbackBar mFeedbackBar;
    protected ExpandableFab mExpandableFab;
    @Inject
    AppContainer mAppContainer;
    @Inject
    AccountManager mAccountManager;
    private Menu mMenu;
    private ObjectGraph mActivityGraph;
    private boolean mMenuIsCleared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieReminderApplication application = MovieReminderApplication.get(this);
        mActivityGraph = application.getApplicationObjectGraph().plus(getModules().toArray());
        mActivityGraph.inject(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerLayout.getDrawerLockMode(GravityCompat.START) != MovieReminderDrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mDrawerLayout.syncState();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Tracking - activityStart");
    }

    @Override
    protected void onStop() {
        if (BuildConfig.DEBUG)
            Log.d(MovieReminderApplication.TAG, "Tracking - activityStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Eagerly clear the reference to the activity graph to allow it to be garbage collected as
        // soon as possible.
        mActivityGraph = null;
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.mMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
                return false;
            }
            if (mDrawerLayout.getDrawerLockMode(GravityCompat.START) != MovieReminderDrawerLayout.LOCK_MODE_LOCKED_CLOSED && mDrawerLayout.onOptionsItemSelected(item)) {
                return true;
            }

            if (mDrawerLayout.isDrawerOpen(GravityCompat.START) || mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                return false;
            }

            checkBackStackEntryOnBackPress();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerLayout.getDrawerLockMode(GravityCompat.START) != MovieReminderDrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mDrawerLayout.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START) || mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawers();
        } else {
            checkBackStackEntryOnBackPress();
            super.onBackPressed();
        }
    }

    protected void checkBackStackEntryOnBackPress() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount <= 1) {
            finish();
        } else {
            if (observeBackStackEntryOnBackPress()) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mDrawerLayout = mAppContainer.setContentView(this, MovieReminderApplication.get(this), layoutResID, false);
        mToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
    }

    @Override
    public void setActivityContent(int layoutResID) {
        super.setContentView(layoutResID);
        mFeedbackBar = (FeedbackBar) findViewById(R.id.feedback_bar);
        mExpandableFab = (ExpandableFab) findViewById(R.id.expandable_fab);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        clearMenu();
        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentByTag(NavigationFragment.TAG);
        if (fragment != null) {
            fragment.onDrawerOpened(drawerView);
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        initializeMenu();
        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentByTag(NavigationFragment.TAG);
        if (fragment != null) {
            fragment.onDrawerClosed(drawerView);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentByTag(NavigationFragment.TAG);
        if (fragment != null) {
            fragment.onDrawerSlide(drawerView, slideOffset);
        }
    }

    protected void addNavigationDrawerComponent() {
        getAppContainer().addNavigationDrawerComponent(this, MovieReminderApplication.get(this), this);
    }

    protected boolean observeBackStackEntryOnBackPress() {
        return true;
    }

    public AppContainer getAppContainer() {
        return mAppContainer;
    }

    public AccountManager getAccountManager() {
        return mAccountManager;
    }

    public void inject(Object object) {
        mActivityGraph.inject(object);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new ActivityModule(this));
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public MovieReminderDrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public FeedbackBar getFeedbackBar() {
        return mFeedbackBar;
    }

    public ExpandableFab getExpandableFab() {
        return mExpandableFab;
    }

    public void clearMenu() {
        mMenuIsCleared = true;
        mMenu.clear();
    }

    public void initializeMenu() {
        if (mMenuIsCleared) {
            invalidateOptionsMenu();
            mMenuIsCleared = false;
        }
    }
}

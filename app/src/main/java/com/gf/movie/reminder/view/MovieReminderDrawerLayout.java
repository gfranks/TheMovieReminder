package com.gf.movie.reminder.view;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.AttributeSet;
import android.view.MenuItem;

public class MovieReminderDrawerLayout extends DrawerLayout {

    private ActionBarDrawerToggle mDrawerToggle;

    public MovieReminderDrawerLayout(Context context) {
        super(context);
    }

    public MovieReminderDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieReminderDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ActionBarDrawerToggle getDrawerListener() {
        return mDrawerToggle;
    }

    public void setDrawerListener(ActionBarDrawerToggle listener) {
        super.setDrawerListener(listener);
        this.mDrawerToggle = listener;
    }

    public void syncState() {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
}

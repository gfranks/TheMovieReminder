package com.gf.movie.reminder.util;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class AppContainerActionBarDrawerToggle extends BlurActionBarDrawerToggle {

    AppContainerActionBarDrawerToggleListener mListener;

    public AppContainerActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout);
    }

    public AppContainerActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, AppContainerActionBarDrawerToggleListener listener) {
        super(activity, drawerLayout);
        this.mListener = listener;
    }

    public void setListener(AppContainerActionBarDrawerToggleListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (mListener != null) {
            mListener.onDrawerOpened(drawerView);
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (mListener != null) {
            mListener.onDrawerClosed(drawerView);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (mListener != null) {
            mListener.onDrawerSlide(drawerView, slideOffset);
        }
    }
}
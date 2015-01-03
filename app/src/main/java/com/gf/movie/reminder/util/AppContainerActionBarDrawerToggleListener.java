package com.gf.movie.reminder.util;

import android.view.View;

public interface AppContainerActionBarDrawerToggleListener {
    void onDrawerOpened(View drawerView);

    void onDrawerClosed(View drawerView);

    void onDrawerSlide(View drawerView, float slideOffset);
}

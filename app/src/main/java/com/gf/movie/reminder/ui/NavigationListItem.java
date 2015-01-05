package com.gf.movie.reminder.ui;

import android.content.Context;

import com.gf.movie.reminder.R;

public enum NavigationListItem {
    MOVIE_TRAILERS(R.string.nav_movie_trailer),
    MOVIE_REMINDERS(R.string.nav_movie_reminders),
    GAME_TRAILERS(R.string.nav_game_trailer),
    GAME_REMINDERS(R.string.nav_game_reminders),
    LOGIN(R.string.nav_login),
    COPYRIGHT(R.string.copyright);

    int mTitleResId;

    NavigationListItem(int titleResId) {
        this.mTitleResId = titleResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public String getTitle(Context context) {
        return context.getString(mTitleResId);
    }
}

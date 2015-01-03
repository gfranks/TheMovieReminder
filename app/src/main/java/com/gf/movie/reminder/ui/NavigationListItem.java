package com.gf.movie.reminder.ui;

import android.content.Context;

import com.gf.movie.reminder.R;

public enum NavigationListItem {
    TRAILER(R.string.nav_trailer),
    REMINDERS(R.string.nav_reminders),
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

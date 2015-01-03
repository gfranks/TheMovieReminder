package com.gf.movie.reminder.adapter;

import android.widget.ArrayAdapter;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.ui.NavigationListItem;
import com.gf.movie.reminder.util.AccountManager;

import javax.inject.Inject;

public class NavigationListAdapter extends ArrayAdapter {

    @Inject
    AccountManager mAccountManager;

    public NavigationListAdapter(BaseActivity activity) {
        super(activity, android.R.layout.simple_list_item_1);
        activity.inject(this);
    }

    @Override
    public int getCount() {
        return NavigationListItem.values().length - 1;
    }

    @Override
    public Object getItem(int position) {
        NavigationListItem item = NavigationListItem.values()[position];
        if (item == NavigationListItem.LOGIN) {
            if (mAccountManager.isLoggedIn()) {
                return getContext().getString(R.string.nav_logout);
            } else {
                return getContext().getString(R.string.nav_login);
            }
        }
        return getContext().getString(NavigationListItem.values()[position].getTitleResId());
    }
}

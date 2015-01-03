package com.gf.movie.reminder.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.ui.ExpandableFab;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.MovieReminderDrawerLayout;

import javax.inject.Inject;

public class BaseFragment extends Fragment {

    @Inject
    AccountManager mAccountManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).inject(this);
    }

    public AccountManager getAccountManager() {
        return mAccountManager;
    }

    public Toolbar getToolbar() {
        return ((BaseActivity) getActivity()).getToolbar();
    }

    public MovieReminderDrawerLayout getDrawerLayout() {
        return ((BaseActivity) getActivity()).getDrawerLayout();
    }

    public FeedbackBar getFeedbackBar() {
        return ((BaseActivity) getActivity()).getFeedbackBar();
    }

    public ExpandableFab getExpandableFab() {
        return ((BaseActivity) getActivity()).getExpandableFab();
    }

    public void notifyUi() {
    }

    public void onFabClick() {
    }
}

package com.gf.movie.reminder.fragment.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.ui.AppContainer;
import com.gf.movie.reminder.ui.ExpandableFab;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.util.BlurDialogFragmentHelper;
import com.gf.movie.reminder.view.FeedbackBar;
import com.gf.movie.reminder.view.MovieReminderDrawerLayout;

import javax.inject.Inject;

public class BaseDialogFragment extends DialogFragment {

    @Inject
    AccountManager mAccountManager;

    private BlurDialogFragmentHelper mHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new BlurDialogFragmentHelper(this);
        mHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelper.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper.onStart();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mHelper.onCancel(dialog);
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mHelper.onDismiss(dialog);
        super.onDismiss(dialog);
    }

    public AccountManager getAccountManager() {
        return mAccountManager;
    }

    public AppContainer getAppContainer() {
        return ((BaseActivity) getActivity()).getAppContainer();
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
}

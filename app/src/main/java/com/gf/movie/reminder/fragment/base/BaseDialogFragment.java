package com.gf.movie.reminder.fragment.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.util.AccountManager;
import com.gf.movie.reminder.util.BlurDialogFragmentHelper;

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

    public AccountManager getAccountManager() {
        return mAccountManager;
    }
}

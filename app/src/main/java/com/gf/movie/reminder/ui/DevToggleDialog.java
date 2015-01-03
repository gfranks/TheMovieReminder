package com.gf.movie.reminder.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.activity.base.BaseActivity;
import com.gf.movie.reminder.application.MovieReminderApplication;

public class DevToggleDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener,
        DialogInterface.OnDismissListener {

    private final String DEVOPER_ENABLE_STRING = "@LGFZ71";

    private BaseActivity mActivity;
    private EditText mInput;
    private boolean mDevSettingsEnabled;

    public DevToggleDialog(BaseActivity activity) {
        super(activity);
        this.mActivity = activity;
        setTitle(R.string.dev_toggle_dialog_title);
        setMessage(R.string.dev_toggle_dialog_message);
        mInput = new EditText(activity);
        setView(mInput);
        setPositiveButton(R.string.ok, this);
        setNegativeButton(R.string.cancel, this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            if (mInput.getText().toString().equals(DEVOPER_ENABLE_STRING)) {
                mActivity.getSharedPreferences(MovieReminderApplication.TAG, Context.MODE_PRIVATE).edit().putBoolean("DEV_SETTINGS_ENABLED", true).apply();
                mActivity.getAppContainer().addDevSettingsDrawerComponent(mActivity);
                mDevSettingsEnabled = true;
            }
        }
    }

    @NonNull
    @Override
    public AlertDialog create() {
        AlertDialog dialog = super.create();
        dialog.setOnDismissListener(this);
        return dialog;
    }

    @NonNull
    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();
        dialog.setOnDismissListener(this);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDevSettingsEnabled) {
            mActivity.getFeedbackBar().showInfo(R.string.dev_toggle_dialog_success, false);
            mActivity = null;
        }
    }
}
